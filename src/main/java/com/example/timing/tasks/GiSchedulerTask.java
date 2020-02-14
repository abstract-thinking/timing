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
        final YearMonth now = YearMonth.now();

        InterestRates interestRates = new InterestRates(ratesService.fetchInterestRates());
        Rates exchangeRates = new Rates(ratesService.fetchExchangeRates());
        Rates inflationRates = new Rates(ratesService.fetchInflationRates());

        PartialIndicatorResult interestResult = interestRates.calculate(now);
        PartialIndicatorResult seasonResult = new Season().calculate(now);
        PartialIndicatorResult exchangeResult = exchangeRates.calculate(now.minusMonths(1));
        PartialIndicatorResult inflationResult = inflationRates.calculate(now.minusMonths(2));

        final int sumOfPointsThisMonth = seasonResult.getPoint() +
                inflationResult.getPoint() + interestResult.getPoint() + exchangeResult.getPoint();

        IndicatorResult result = IndicatorResult.builder()
                .date(now.atDay(1))
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

        // TODO: Update last entry of MongoDB before inserting
        // TODO: Update csv: add new line and correct line before
        // https://www.technicalkeeda.com/java-8-tutorials/java-8-stream-find-and-replace-file-content

        repository.save(result);
    }

    private boolean decideInvestment(int sumOfPoints) {
        if (sumOfPoints > 2) {
            return true;
        } else if (sumOfPoints < 2) {
            return false;
        } else { // currentPoints == 2
            return repository.findBySumOfPointsIsNotOrderByDateDesc(2).get(0).shouldInvest();
        }
    }
}
