package com.example.timing.control.gi;

import com.example.timing.boundary.gi.IndicatorResult;
import com.example.timing.services.rates.RatesService;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import static java.time.Month.JANUARY;
import static java.util.Comparator.comparing;

@Service
public class GiCalculator {

    private final RatesService ratesService;

    public GiCalculator(RatesService ratesService) {
        this.ratesService = ratesService;
    }

    public List<IndicatorResult> calculate() {
        SeasonIndicator seasonIndicator = new SeasonIndicator();
        InterestRatesIndicator interestRatesIndicator = new InterestRatesIndicator(ratesService.fetchInterestRates());
        RatesIndicator exchangeRatesIndicator = new RatesIndicator(ratesService.fetchExchangeRates());
        RatesIndicator inflationRatesIndicator = new RatesIndicator(ratesService.fetchInflationRates());

        final YearMonth startDate = YearMonth.of(2001, JANUARY);
        final YearMonth endDate = YearMonth.now().plusMonths(1);

        IndicatorResult previousResult = null;
        List<IndicatorResult> results = new ArrayList<>();
        for (YearMonth date = startDate; date.isBefore(endDate); date = date.plusMonths(1)) {
            PartialIndicatorResult seasonResult = seasonIndicator.indicate(date);
            PartialIndicatorResult interestResult = interestRatesIndicator.indicate(date);
            PartialIndicatorResult exchangeResultOneMonthAgo = exchangeRatesIndicator.indicate(date.minusMonths(1));
            PartialIndicatorResult inflationResultOneMonthAgo = inflationRatesIndicator.indicate(date.minusMonths(2));

            IndicatorResult result = createIndicatorResult(date,
                    interestResult,
                    seasonResult,
                    exchangeResultOneMonthAgo,
                    inflationResultOneMonthAgo,
                    previousResult != null && previousResult.shouldInvest());
            results.add(result);
            previousResult = result;
        }

        results.sort(comparing(IndicatorResult::getDate).reversed());

        return results;
    }

    private IndicatorResult createIndicatorResult(
            YearMonth date,
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
