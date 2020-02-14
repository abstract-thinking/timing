package com.example.timing.utils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import static java.time.format.DateTimeFormatter.ISO_DATE;

public final class RateCsvParser {

    public static Map<LocalDate, Double> parseInterestRates(String data) {
        Map<LocalDate, Double> interest = new HashMap<>();

        Scanner scanner = new Scanner(data);
        scanner.useDelimiter(",");
        while (scanner.hasNext()) {
            String lineOfText = scanner.nextLine();
            if (!Character.isDigit(lineOfText.charAt(0))) {
                continue;
            }

            String[] split = lineOfText.split(",");
            interest.put(LocalDate.parse(split[0], ISO_DATE), Double.valueOf(split[1]));
        }
        scanner.close();

        return interest;
    }

    public static Map<LocalDate, Double> parseInterestRates(String data, int index) {
        Map<LocalDate, Double> interest = new HashMap<>();

        Scanner scanner = new Scanner(data);
        scanner.useDelimiter(",");
        while (scanner.hasNext()) {
            String lineOfText = scanner.nextLine();
            if (!Character.isDigit(lineOfText.charAt(0))) {
                continue;
            }

            String[] split = lineOfText.split(",");
            interest.put(YearMonth.parse(split[0]).atDay(1), Double.valueOf(split[index]));
        }
        scanner.close();

        return interest;
    }

    public static Map<YearMonth, Double> parseRates(String data) {
        Map<YearMonth, Double> result = new HashMap<>();

        Scanner scanner = new Scanner(data);
        scanner.useDelimiter(",");

        while (scanner.hasNext()) {
            String lineOfText = scanner.nextLine();
            if (!Character.isDigit(lineOfText.charAt(0))) {
                continue;
            }

            String[] split = lineOfText.split(",");
            result.put(
                    YearMonth.parse(split[0], DateTimeFormatter.ofPattern("yyyyMMM", Locale.ENGLISH)),
                    Double.valueOf(split[1]));
        }
        scanner.close();

        return result;
    }

    public static Map<YearMonth, Double> parseRates(String data, int index) {
        Map<YearMonth, Double> result = new HashMap<>();

        Scanner scanner = new Scanner(data);
        scanner.useDelimiter(",");

        while (scanner.hasNext()) {
            String lineOfText = scanner.nextLine();
            if (!Character.isDigit(lineOfText.charAt(0))) {
                continue;
            }

            String[] split = lineOfText.split(",");
            result.put(YearMonth.parse(split[0]), Double.valueOf(split[index]));
        }
        scanner.close();

        return result;
    }
}