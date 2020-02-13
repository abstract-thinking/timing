package com.example.timing.tasks;

import com.example.timing.InterestRates;
import com.example.timing.PartialIndicatorResult;
import com.example.timing.Rates;
import com.example.timing.Season;
import com.example.timing.data.GiRepository;
import com.example.timing.data.IndicatorResult;
import com.example.timing.web.RatesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;

@Slf4j
@Component
public class GiSchedulerTask {

    private final RatesService ratesService;

    private final GiRepository repository;

    @Autowired
    public GiSchedulerTask(GiRepository repository, RatesService ratesService) {
        this.repository = repository;
        this.ratesService = ratesService;
    }

    @Scheduled(cron = "0 0 0 1 1/1 *")
    public void processGi() {
        final LocalDate now = LocalDate.now();

        InterestRates interestRates = new InterestRates(ratesService.processInterestRates());
        PartialIndicatorResult interestResult = interestRates.calculate(YearMonth.from(now));

        PartialIndicatorResult seasonResult = new Season().calculate(YearMonth.from(now));

        Rates exchangeRates = new Rates(ratesService.processExchangeRates());
        PartialIndicatorResult exchangeResult = exchangeRates.calculate(YearMonth.from(now).minusMonths(1));

        Rates inflationRates = new Rates(ratesService.processInflationRates());
        PartialIndicatorResult inflationResult = inflationRates.calculate(YearMonth.from(now).minusMonths(2));

        final int sumOfPointsThisMonth = seasonResult.getPoint() +
                inflationResult.getPoint() + interestResult.getPoint() + exchangeResult.getPoint();

        IndicatorResult result = IndicatorResult.builder()
                .date(now)
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
                .shouldInvest(decideInvestment(sumOfPointsThisMonth))
                .build();

        repository.save(result);
    }

    private boolean decideInvestment(int sumOfPoints) {
        if (sumOfPoints > 2) {
            return true;
        } else if (sumOfPoints < 2) {
            return false;
        } else { // currentPoints == 2
            return repository.findBySumOfPointsIsNot(2).get(0).shouldInvest();
        }
    }
}
