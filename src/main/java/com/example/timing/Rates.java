package com.example.timing;

import lombok.Getter;

import java.time.YearMonth;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public final class Rates implements Point {

    private final Map<YearMonth, Double> rates;

    @Getter
    private final YearMonth date;

    @Getter
    private final YearMonth comparativeDate;

    public Rates(Map<YearMonth, Double> rates, YearMonth date) {
        this.rates = new TreeMap<>(Comparator.reverseOrder());
        this.rates.putAll(rates);

        this.date = date;
        this.comparativeDate = date.minusYears(1);
    }

    public double getRate() {
        return rates.get(date);
    }

    public double getComparativeRate() {
        return rates.get(comparativeDate);
    }

    public boolean isDecreased() {
        Double currentRate = rates.get(date);
        Double comparativeRate = rates.get(comparativeDate);

        return currentRate < comparativeRate;
    }

    @Override
    public int getPoint() {
        return isDecreased() ? 1 : 0;
    }

}
