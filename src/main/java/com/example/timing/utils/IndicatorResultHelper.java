package com.example.timing.utils;

import com.example.timing.data.GiRepository;
import com.example.timing.indicator.IndicatorResult;
import com.example.timing.indicator.PartialIndicatorResult;

import java.time.YearMonth;

public class IndicatorResultHelper {

    public static IndicatorResult createIndicatorResult(
            YearMonth date,
            PartialIndicatorResult interestResult,
            PartialIndicatorResult seasonResult,
            PartialIndicatorResult exchangeResult,
            PartialIndicatorResult inflationResult,
            GiRepository repository) {
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
                .shouldInvest(decideInvestment(sumOfPointsThisMonth, repository))
                .build();
    }

    private static boolean decideInvestment(int sumOfPoints, GiRepository repository) {
        if (sumOfPoints > 2) {
            return true;
        } else if (sumOfPoints < 2) {
            return false;
        } else { // currentPoints == 2
            return repository.findBySumOfPointsIsNotOrderByDateDesc(2).get(0).shouldInvest();
        }
    }
}
