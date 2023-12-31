package com.example.timing.services.rates;

import com.example.timing.control.gi.domain.DailyUSDollarEuroInterestRate;
import com.example.timing.control.gi.domain.Status;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static java.time.format.DateTimeFormatter.ISO_DATE;

public final class RateCsvParser {

    public static Map<LocalDate, Double> parseInterestRates(String data) {
        Map<LocalDate, Double> interestRates = new HashMap<>();

        try (Scanner scanner = new Scanner(data)) {
            scanner.useDelimiter(",");

            // Skip header
            scanner.nextLine();
            while (scanner.hasNext()) {
                String[] split = scanner.nextLine().split(",");
                interestRates.put(LocalDate.parse(split[8], ISO_DATE), Double.valueOf(split[9]));
            }
        }

        return interestRates;
    }

    public static Map<LocalDate, DailyUSDollarEuroInterestRate> parseDailyExchangeRates(String data) {
        Map<LocalDate, DailyUSDollarEuroInterestRate> dailyExchangeRates = new HashMap<>();

        try (Scanner scanner = new Scanner(data)) {
            scanner.useDelimiter(",");

            // Skip header
            scanner.nextLine();
            while (scanner.hasNext()) {
                String[] split = scanner.nextLine().split(",");
                dailyExchangeRates.put(LocalDate.parse(split[6], ISO_DATE), createRate(split[7]));
            }
        }

        return dailyExchangeRates;
    }

    private static DailyUSDollarEuroInterestRate createRate(String rate) {
        return rate.isEmpty() ? new DailyUSDollarEuroInterestRate(Double.NaN, Status.MISSING) :
                new DailyUSDollarEuroInterestRate(Double.valueOf(rate), Status.NORMAL);
    }

    public static Map<YearMonth, Double> parseMonthlyExchangeRates(String data) {
        Map<YearMonth, Double> monthlyExchangeRates = new HashMap<>();

        try (Scanner scanner = new Scanner(data)) {
            scanner.useDelimiter(",");

            // Skip header
            scanner.nextLine();
            while (scanner.hasNext()) {
                String[] split = scanner.nextLine().split(",");
                monthlyExchangeRates.put(YearMonth.parse(split[6], DateTimeFormatter.ofPattern("yyyy-MM")), Double.valueOf(split[7]));
            }
        }

        return monthlyExchangeRates;
    }

    public static Map<YearMonth, Double> parseInflationRates(String data) {
        Map<YearMonth, Double> inflationRates = new HashMap<>();

        try (Scanner scanner = new Scanner(data)) {
            scanner.useDelimiter(",");

            // Skip header
            scanner.nextLine();
            while (scanner.hasNext()) {
                String[] split = scanner.nextLine().split(",");
                inflationRates.put(YearMonth.parse(split[7], DateTimeFormatter.ofPattern("yyyy-MM")), Double.valueOf(split[8]));
            }
        }

        return inflationRates;
    }
}