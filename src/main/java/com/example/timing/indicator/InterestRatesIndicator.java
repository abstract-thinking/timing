package com.example.timing.indicator;

import com.example.timing.results.PartialIndicatorResult;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class InterestRatesIndicator implements Indicator {

    private final TreeMap<LocalDate, Double> rates;

    public InterestRatesIndicator(Map<LocalDate, Double> rates) {
        this.rates = new TreeMap<>(Comparator.reverseOrder());
        this.rates.putAll(rates);
    }

    @Override
    public PartialIndicatorResult indicate(YearMonth date) {
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
