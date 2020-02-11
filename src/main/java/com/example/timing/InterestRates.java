package com.example.timing;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class InterestRates implements Calculator {

    private final TreeMap<LocalDate, Double> rates;

    public InterestRates(Map<LocalDate, Double> rates) {
        this.rates = new TreeMap<>(Comparator.reverseOrder());
        this.rates.putAll(rates);
    }

    public boolean isDecreased() {
        List<Double> rates = new ArrayList<>(this.rates.values());

        Double rateOfLastDecision = rates.get(0);
        for (int i = 1; i < rates.size(); ++i) {
            if (rates.get(i).compareTo(rateOfLastDecision) != 0) {
                return rateOfLastDecision < rates.get(i);
            }
        }

        throw new IllegalStateException("Unexpected behaviour");
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
