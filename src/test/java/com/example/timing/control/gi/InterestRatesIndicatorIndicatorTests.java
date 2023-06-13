package com.example.timing.control.gi;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;

import static com.example.timing.utils.RatesGenerator.generateInterestRatesAsc;
import static com.example.timing.utils.RatesGenerator.generateInterestRatesDesc;
import static java.time.Month.APRIL;
import static java.time.Month.DECEMBER;
import static java.time.Month.MAY;
import static java.time.Month.SEPTEMBER;
import static org.assertj.core.api.Assertions.assertThat;

public class InterestRatesIndicatorIndicatorTests {

    @Test
    public void shouldReturnAPointBecauseRateDecreased() {
        InterestRatesIndicator interestRatesIndicator = new InterestRatesIndicator(generateInterestRatesDesc());

        PartialIndicatorResult result = interestRatesIndicator.indicate(LocalDate.now());

        assertThat(result).isEqualTo(
                createGiResult(
                        LocalDate.of(2019, SEPTEMBER, 18), 0.0,
                        LocalDate.of(2015, SEPTEMBER, 12), 0.05,
                        1));
    }

    @Test
    public void shouldNotReturnAPointBecauseRateIncreased() {
        InterestRatesIndicator interestRatesIndicator = new InterestRatesIndicator(generateInterestRatesAsc());

        PartialIndicatorResult result = interestRatesIndicator.indicate(LocalDate.now());

        assertThat(result).isEqualTo(
                createGiResult(
                        LocalDate.of(2019, SEPTEMBER, 18), 0.1,
                        LocalDate.of(2015, MAY, 12), 0.05,
                        0));
    }

    @Test
    public void shouldReturnAPointBecauseRateDecreasedInYear2010() {
        InterestRatesIndicator interestRatesIndicator = new InterestRatesIndicator(generateInterestRatesDesc());

        PartialIndicatorResult result = interestRatesIndicator.indicate(LocalDate.of(2010, DECEMBER, 1));

        assertThat(result).isEqualTo(
                createGiResult(
                        LocalDate.of(2009, MAY, 13), 1.0,
                        LocalDate.of(2009, APRIL, 8), 1.25,
                        1));
    }

    private static PartialIndicatorResult createGiResult(
            LocalDate date, double rate, LocalDate comparativeDate, double comparativeRate, int point) {
        return PartialIndicatorResult.builder()
                .date(date)
                .rate(rate)
                .comparativeDate(comparativeDate)
                .comparativeRate(comparativeRate)
                .point(point)
                .build();
    }
}
