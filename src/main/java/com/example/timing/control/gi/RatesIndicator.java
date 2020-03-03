package com.example.timing.control.gi;

import java.time.YearMonth;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

final class RatesIndicator implements Indicator {

    private final Map<YearMonth, Double> rates;

    public RatesIndicator(Map<YearMonth, Double> rates) {
        this.rates = new TreeMap<>(Comparator.reverseOrder());
        this.rates.putAll(rates);
    }

    @Override
    public PartialIndicatorResult indicate(YearMonth date) {
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
