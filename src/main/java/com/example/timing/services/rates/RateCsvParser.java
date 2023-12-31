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
        Map<LocalDate, Double> interest = new HashMap<>();

        try (Scanner scanner = new Scanner(data)) {
            scanner.useDelimiter(",");

            // Skip header
            scanner.nextLine();
            while (scanner.hasNext()) {
                String[] split = scanner.nextLine().split(",");
                interest.put(LocalDate.parse(split[8], ISO_DATE), Double.valueOf(split[9]));
            }
        }

        return interest;
    }

    public static Map<LocalDate, DailyUSDollarEuroInterestRate> parseDailyInterestRates(String data) {
        Map<LocalDate, DailyUSDollarEuroInterestRate> interest = new HashMap<>();

        try (Scanner scanner = new Scanner(data)) {
            scanner.useDelimiter(",");

            // Skip header
            scanner.nextLine();
            while (scanner.hasNext()) {
                String[] split = scanner.nextLine().split(",");
                LocalDate date = LocalDate.parse(split[6], ISO_DATE);
                String rate = split[7];
                if (rate.isEmpty()) {
                    interest.put(date, new DailyUSDollarEuroInterestRate(Double.NaN, Status.MISSING));
                } else {
                    interest.put(date, new DailyUSDollarEuroInterestRate(Double.valueOf(rate), Status.NORMAL));
                }
            }
        }

        return interest;
    }

    public static Map<YearMonth, Double> parseMonthlyInterestRates(String data) {
        Map<YearMonth, Double> result = new HashMap<>();

        try (Scanner scanner = new Scanner(data)) {
            scanner.useDelimiter(",");

            // Skip header
            scanner.nextLine();
            while (scanner.hasNext()) {
                String[] split = scanner.nextLine().split(",");
                result.put(YearMonth.parse(split[6], DateTimeFormatter.ofPattern("yyyy-MM")), Double.valueOf(split[7]));
            }
        }

        return result;
    }

    public static Map<YearMonth, Double> parseInflationRates(String data) {
        Map<YearMonth, Double> result = new HashMap<>();

        try (Scanner scanner = new Scanner(data)) {
            scanner.useDelimiter(",");

            // Skip header
            scanner.nextLine();
            while (scanner.hasNext()) {
                String[] split = scanner.nextLine().split(",");
                result.put(YearMonth.parse(split[7], DateTimeFormatter.ofPattern("yyyy-MM")), Double.valueOf(split[8]));
            }
        }

        return result;
    }
}