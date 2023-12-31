package com.example.timing.control.gi;

import com.example.timing.control.gi.domain.DailyUSDollarEuroInterestRate;
import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Objects.isNull;

@Slf4j
final class WeeklyRatesIndicator implements Indicator {

    private final Map<LocalDate, DailyUSDollarEuroInterestRate> rates;

    public WeeklyRatesIndicator(Map<LocalDate, DailyUSDollarEuroInterestRate> rates) {
        this.rates = new TreeMap<>(Comparator.reverseOrder());
        this.rates.putAll(rates);
    }

    @Override
    public PartialIndicatorResult indicate(LocalDate date) {
        RateResult rateResult = determineRate(previousOrSameFriday(date));
        RateResult comparativeRateResult= determineRate(previousOrSameFriday(date.minusYears(1)));

        return PartialIndicatorResult.builder()
                .date(rateResult.date())
                .rate(rateResult.rate().value())
                .comparativeDate(comparativeRateResult.date())
                .comparativeRate(comparativeRateResult.rate().value())
                .point(rateResult.rate().value() < comparativeRateResult.rate().value() ? 1 : 0)
                .build();
    }

    private RateResult determineRate(LocalDate date) {
        DailyUSDollarEuroInterestRate rate = rates.get(date);
        // Note there is a break inside the ECB API.
        // First the date for public holiday was missing, later they added the date with a hint that's a public holiday
        while (isNull(rate) || rate.isMissing()) {
            date = date.minusDays(1);
            log.info("Correcting value: New date: " + date);
            rate = rates.get(date);
        }

        return new RateResult(date, rate);
    }

    record RateResult(LocalDate date, DailyUSDollarEuroInterestRate rate) {}

    private static LocalDate previousOrSameFriday(LocalDate localDate) {
        return localDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.FRIDAY));
    }

}
