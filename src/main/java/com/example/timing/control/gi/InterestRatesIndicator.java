package com.example.timing.control.gi;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;

class InterestRatesIndicator implements Indicator {

    private final NavigableMap<LocalDate, Double> rates;

    public InterestRatesIndicator(Map<LocalDate, Double> rates) {
        this.rates = new TreeMap<>(Comparator.reverseOrder());
        this.rates.putAll(rates);

    }

    @Override
    public PartialIndicatorResult indicate(LocalDate date) {
        Map.Entry<LocalDate, Double> firstEntry = rates.higherEntry(date);

        Map.Entry<LocalDate, Double> comparativeEntry = firstEntry;

        SortedMap<LocalDate, Double> tailMap = rates.tailMap(date, false);
        for (Map.Entry<LocalDate, Double> rate : tailMap.entrySet()) {
            if (!firstEntry.getValue().equals(rate.getValue())) {
                comparativeEntry = rate;
                break;
            }
        }

        return PartialIndicatorResult.builder()
                .date(firstEntry.getKey())
                .rate(firstEntry.getValue())
                .comparativeDate(comparativeEntry.getKey())
                .comparativeRate(comparativeEntry.getValue())
                .point(firstEntry.getValue() < comparativeEntry.getValue() ? 1 : 0)
                .build();
    }
}
