package com.example.timing;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class InterestRates implements Point {

    private final Map<LocalDate, Double> rates;

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
    public int getPoint() {
        return isDecreased() ? 1 : 0;
    }
}
