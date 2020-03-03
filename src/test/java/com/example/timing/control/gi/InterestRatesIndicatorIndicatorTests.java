package com.example.timing.control.gi;

import org.junit.jupiter.api.Test;

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

        PartialIndicatorResult result = interestRatesIndicator.indicate(YearMonth.now());

        assertThat(result).isEqualTo(
                createGiResult(
                        YearMonth.of(2019, SEPTEMBER), 0.0,
                        YearMonth.of(2015, SEPTEMBER), 0.05,
                        1));
    }

    @Test
    public void shouldNotReturnAPointBecauseRateIncreased() {
        InterestRatesIndicator interestRatesIndicator = new InterestRatesIndicator(generateInterestRatesAsc());

        PartialIndicatorResult result = interestRatesIndicator.indicate(YearMonth.now());

        assertThat(result).isEqualTo(
                createGiResult(
                        YearMonth.of(2019, SEPTEMBER), 0.1,
                        YearMonth.of(2015, MAY), 0.05,
                        0));
    }

    @Test
    public void shouldReturnAPointBecauseRateDecreasedInYear2010() {
        InterestRatesIndicator interestRatesIndicator = new InterestRatesIndicator(generateInterestRatesDesc());

        PartialIndicatorResult result = interestRatesIndicator.indicate(YearMonth.of(2010, DECEMBER));

        assertThat(result).isEqualTo(
                createGiResult(
                        YearMonth.of(2009, MAY), 1.0,
                        YearMonth.of(2009, APRIL), 1.25,
                        1));
    }

    private PartialIndicatorResult createGiResult(
            YearMonth date, double rate, YearMonth comparativeDate, double comparativeRate, int point) {
        return PartialIndicatorResult.builder()
                .date(date)
                .rate(rate)
                .comparativeDate(comparativeDate)
                .comparativeRate(comparativeRate)
                .point(point)
                .build();
    }
}
