package com.example.timing.control.gi;

import com.example.timing.boundary.gi.api.IndicatorResult;
import com.example.timing.control.gi.domain.DailyUSDollarEuroInterestRate;
import com.example.timing.services.rates.RatesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.time.Month.APRIL;
import static java.time.Month.JANUARY;
import static java.util.Comparator.comparing;

@Slf4j
@Service
public class GiCalculator {

    private final RatesService ratesService;

    public GiCalculator(RatesService ratesService) {
        this.ratesService = ratesService;
    }

    public List<IndicatorResult> calculateMonthly() {
        CompletableFuture<Map<LocalDate, Double>> interestRatesFuture = ratesService.fetchInterestRates();
        CompletableFuture<Map<YearMonth, Double>> exchangeRatesFuture = ratesService.fetchExchangeRatesMonthly();
        CompletableFuture<Map<YearMonth, Double>> inflationRatesFuture = ratesService.fetchInflationRates();

        SeasonIndicator seasonIndicator = new SeasonIndicator();
        final InterestRatesIndicator interestRatesIndicator;
        final MonthlyRatesIndicator exchangeRatesIndicator;
        final MonthlyRatesIndicator inflationRatesIndicator;
        try {
            interestRatesIndicator = new InterestRatesIndicator(interestRatesFuture.get());
            exchangeRatesIndicator = new MonthlyRatesIndicator(exchangeRatesFuture.get());
            inflationRatesIndicator = new MonthlyRatesIndicator(inflationRatesFuture.get());
        } catch (ExecutionException | InterruptedException ex) {
            log.error("Fetching data failed!", ex);
            throw new RuntimeException("Fetching data failed");
        }

        // First Monday seems to be ok because all data are published
        final LocalDate startDate = LocalDate.of(2001, JANUARY, 1);
        final LocalDate endDate = LocalDate.now();

        IndicatorResult previousResult = null;
        List<IndicatorResult> results = new ArrayList<>();
        for (LocalDate date = startDate; date.isBefore(endDate) || date.isEqual(endDate); date = date.plusMonths(1)) {
            PartialIndicatorResult seasonResult = seasonIndicator.indicate(date);
            PartialIndicatorResult interestResult = interestRatesIndicator.indicate(date);
            PartialIndicatorResult exchangeResultOneMonthAgo = exchangeRatesIndicator.indicate(date.minusMonths(1));
            PartialIndicatorResult inflationResulTwoMonthAgo = inflationRatesIndicator.indicate(date.minusMonths(2));

            IndicatorResult result = createIndicatorResult(date,
                    interestResult,
                    seasonResult,
                    exchangeResultOneMonthAgo,
                    inflationResulTwoMonthAgo,
                    previousResult != null && previousResult.shouldInvest());
            results.add(result);
            previousResult = result;
        }

        results.sort(comparing(IndicatorResult::getDate).reversed());

        return results;
    }

    public List<IndicatorResult> calculateWeekly() {
        CompletableFuture<Map<LocalDate, Double>> interestRatesFuture = ratesService.fetchInterestRates();
        CompletableFuture<Map<LocalDate, DailyUSDollarEuroInterestRate>> exchangeRatesFuture = ratesService.fetchExchangeRatesDaily();
        CompletableFuture<Map<YearMonth, Double>> inflationRatesFuture = ratesService.fetchInflationRates();

        SeasonIndicator seasonIndicator = new SeasonIndicator();
        final InterestRatesIndicator interestRatesIndicator;
        final WeeklyRatesIndicator exchangeRatesIndicator;
        final MonthlyRatesIndicator inflationRatesIndicator;
        try {
            interestRatesIndicator = new InterestRatesIndicator(interestRatesFuture.get());
            exchangeRatesIndicator = new WeeklyRatesIndicator(exchangeRatesFuture.get());
            inflationRatesIndicator = new MonthlyRatesIndicator(inflationRatesFuture.get());
        } catch (ExecutionException | InterruptedException ex) {
            log.error("Fetching data failed!", ex);
            throw new RuntimeException("Fetching data failed");
        }

        // Saturday seems to be ok because Friday is the deadline for exchange
        final LocalDate startDate = LocalDate.of(2001, APRIL, 7);
        final LocalDate endDate = previousOrSameSaturday(LocalDate.now());
        log.info("Calculated end date: " + endDate);

        IndicatorResult previousResult = null;
        List<IndicatorResult> results = new ArrayList<>();
        for (LocalDate date = startDate; date.isBefore(endDate) || date.isEqual(endDate); date = date.plusWeeks(1)) {
            PartialIndicatorResult seasonResult = seasonIndicator.indicate(date);
            PartialIndicatorResult interestResult = interestRatesIndicator.indicate(date);
            PartialIndicatorResult exchangeResult = exchangeRatesIndicator.indicate(date);
            PartialIndicatorResult inflationResultTwoMonthAgo =
                    inflationRatesIndicator.indicate(date.minusMonths(2));

            IndicatorResult result = createIndicatorResult(date,
                    interestResult,
                    seasonResult,
                    exchangeResult,
                    inflationResultTwoMonthAgo,
                    previousResult != null && previousResult.shouldInvest());
            results.add(result);
            previousResult = result;
        }

        results.sort(comparing(IndicatorResult::getDate).reversed());

        return results;
    }

    private static LocalDate previousOrSameSaturday(LocalDate localDate) {
        return localDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SATURDAY));
    }

    private IndicatorResult createIndicatorResult(
            LocalDate date,
            PartialIndicatorResult interestResult,
            PartialIndicatorResult seasonResult,
            PartialIndicatorResult exchangeResult,
            PartialIndicatorResult inflationResult,
            boolean lastResult) {
        final int sumOfPointsThisMonth = seasonResult.getPoint() +
                inflationResult.getPoint() + interestResult.getPoint() + exchangeResult.getPoint();

        return IndicatorResult.builder()
                .date(date)
                .seasonPoint(seasonResult.getPoint())
                .interestRate(interestResult.getRate())
                .interestPoint(interestResult.getPoint())
                .exchangeRate(exchangeResult.getRate())
                .exchangeRageOneYearAgo(exchangeResult.getComparativeRate())
                .exchangePoint(exchangeResult.getPoint())
                .inflationRate(inflationResult.getRate())
                .inflationRateOneYearAgo(inflationResult.getComparativeRate())
                .inflationPoint(inflationResult.getPoint())
                .sumOfPoints(sumOfPointsThisMonth)
                .shouldInvest(decideInvestment(sumOfPointsThisMonth, lastResult))
                .build();
    }

    private boolean decideInvestment(int sumOfPoints, boolean lastResult) {
        if (sumOfPoints > 2) {
            return true;
        } else if (sumOfPoints < 2) {
            return false;
        } else { // currentPoints == 2
            return lastResult;
        }
    }
}
