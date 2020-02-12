package com.example.timing;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class InterestRates implements Calculator {

    private final TreeMap<LocalDate, Double> rates;

    public InterestRates(Map<LocalDate, Double> rates) {
        this.rates = new TreeMap<>(Comparator.reverseOrder());
        this.rates.putAll(rates);
    }

    @Override
    public PartialIndicatorResult calculate(YearMonth date) {
        Map.Entry<LocalDate, Double> firstEntry = rates.firstEntry();
        Map.Entry<LocalDate, Double> comparativeEntry = firstEntry;

        for (Map.Entry<LocalDate, Double> rate : rates.entrySet()) {
            if (!firstEntry.getValue().equals(rate.getValue())) {
                comparativeEntry = rate;
                break;
            }
        }

        return PartialIndicatorResult.builder()
                .date(YearMonth.from(firstEntry.getKey()))
                .rate(firstEntry.getValue())
                .comparativeDate(YearMonth.from(comparativeEntry.getKey()))
                .comparativeRate(comparativeEntry.getValue())
                .point(firstEntry.getValue() < comparativeEntry.getValue() ? 1 : 0)
                .build();
    }
}
