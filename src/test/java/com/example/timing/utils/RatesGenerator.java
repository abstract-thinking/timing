package com.example.timing.utils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

public class RatesGenerator {

    public static Map<YearMonth, Double> generateRatesDesc() {
        Map<YearMonth, Double> rates = new HashMap<>();
        rates.put(YearMonth.of(2020, 1), 1.0);
        for (int month = 12; month > 0; --month) {
            rates.put(YearMonth.of(2019, month), month + 1.0);
        }

        return rates;
    }

    public static Map<YearMonth, Double> generateRatesAsc() {
        Map<YearMonth, Double> rates = new HashMap<>();
        rates.put(YearMonth.of(2020, 1), 1.0);
        for (int month = 12; month > 0; --month) {
            rates.put(YearMonth.of(2019, month), month / 10.0);
        }

        return rates;
    }

    public static Map<YearMonth, Double> generateRatesEqual() {
        Map<YearMonth, Double> rates = new HashMap<>();
        rates.put(YearMonth.of(2020, 1), 1.11);
        for (int month = 12; month > 0; --month) {
            rates.put(YearMonth.of(2019, month), 1.11);
        }

        return rates;
    }

    public static Map<LocalDate, Double> generateInterestRatesDesc() {
        Map<LocalDate, Double> interestRates = new HashMap<>();
        interestRates.put(LocalDate.of(2019, 9, 18), 0.00);
        interestRates.put(LocalDate.of(2016, 3, 16), 0.00);
        interestRates.put(LocalDate.of(2015, 9, 12), 0.05);
        interestRates.put(LocalDate.of(2014, 9, 10), 0.05);
        interestRates.put(LocalDate.of(2014, 6, 11), 0.15);
        interestRates.put(LocalDate.of(2013, 11, 13), 0.25);
        interestRates.put(LocalDate.of(2013, 5, 8), 0.5);
        interestRates.put(LocalDate.of(2012, 7, 11), 0.75);
        interestRates.put(LocalDate.of(2011, 11, 9), 1.25);
        interestRates.put(LocalDate.of(2011, 7, 13), 1.5);
        interestRates.put(LocalDate.of(2011, 4, 13), 1.25);
        interestRates.put(LocalDate.of(2009, 5, 13), 1.0);
        interestRates.put(LocalDate.of(2009, 4, 8), 1.25);
        interestRates.put(LocalDate.of(2009, 3, 11), 1.5);

        return interestRates;
    }

    public static Map<LocalDate, Double> generateInterestRatesAsc() {
        Map<LocalDate, Double> interestRates = new HashMap<>();
        interestRates.put(LocalDate.of(2019, 9, 18), 0.10);
        interestRates.put(LocalDate.of(2016, 3, 16), 0.10);
        interestRates.put(LocalDate.of(2015, 5, 12), 0.05);

        return interestRates;
    }
}
