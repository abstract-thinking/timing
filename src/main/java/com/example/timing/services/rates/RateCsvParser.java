package com.example.timing.services.rates;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import static java.time.format.DateTimeFormatter.ISO_DATE;

@Slf4j
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
            try {
                interest.put(LocalDate.parse(split[0], ISO_DATE), Double.valueOf(split[1]));
            } catch (NumberFormatException nfe) {
                log.warn("Can not parse: " + lineOfText);
            }
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
}