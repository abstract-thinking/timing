package com.example.timing.control.gi;

import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
final class WeeklyRatesIndicator implements Indicator {

    private final Map<LocalDate, Double> rates;

    public WeeklyRatesIndicator(Map<LocalDate, Double> rates) {
        this.rates = new TreeMap<>(Comparator.reverseOrder());
        this.rates.putAll(rates);
    }

    @Override
    public PartialIndicatorResult indicate(LocalDate date) {
        log.info("Use date: " + date);
        Double rate = rates.get(date);

        while (rate == null) {
            log.info("Correcting rate");
            date = date.minusDays(1);
            log.info("Day before: " + date);
            rate = rates.get(date);
        }

        LocalDate comparativeDate = previousOrSameFriday(date.minusYears(1));
        log.info("Start date: " + comparativeDate);
        Double comparativeRate = rates.get(comparativeDate);
        while (comparativeRate == null) {
            log.info("Correcting comparative rate");
            comparativeDate = comparativeDate.minusDays(1);
            log.info("Day before: " + comparativeDate);
            comparativeRate = rates.get(comparativeDate);
        }

        return PartialIndicatorResult.builder()
                .date(date)
                .rate(rate)
                .comparativeDate(comparativeDate)
                .comparativeRate(comparativeRate)
                .point(rate < comparativeRate ? 1 : 0)
                .build();
    }

    private static LocalDate previousOrSameFriday(LocalDate localDate) {
        return localDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.FRIDAY));
    }

}
