package com.example.timing;

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
