package com.example.timing.control.gi;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

final class MonthlyRatesIndicator implements Indicator {

    private final Map<YearMonth, Double> rates;

    public MonthlyRatesIndicator(Map<YearMonth, Double> rates) {
        this.rates = new TreeMap<>(Comparator.reverseOrder());
        this.rates.putAll(rates);
    }

    @Override
    public PartialIndicatorResult indicate(LocalDate date) {
        Double rate = rates.get(YearMonth.from(date));

        LocalDate comparativeDate = date.minusYears(1);
        Double comparativeRate = rates.get(YearMonth.from(comparativeDate));

        return PartialIndicatorResult.builder()
                .date(date)
                .rate(rate)
                .comparativeDate(comparativeDate)
                .comparativeRate(comparativeRate)
                .point(rate < comparativeRate ? 1 : 0)
                .build();
    }

}
