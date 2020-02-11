package com.example.timing;

import java.time.YearMonth;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public final class Rates implements Calculator {

    private final Map<YearMonth, Double> rates;

    public Rates(Map<YearMonth, Double> rates) {
        this.rates = new TreeMap<>(Comparator.reverseOrder());
        this.rates.putAll(rates);
    }

    @Override
    public PartialIndicatorResult calculate(YearMonth date) {
        YearMonth comparativeDate = date.minusYears(1);

        Double rate = rates.get(date);
        Double comparativeRate = rates.get(comparativeDate);

        return PartialIndicatorResult.builder()
                .date(date)
                .rate(rate)
                .comparativeDate(comparativeDate)
                .comparativeRate(comparativeRate)
                .point(rate < comparativeRate ? 1 : 0)
                .build();
    }

}
