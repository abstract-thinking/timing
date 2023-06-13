package com.example.timing.control.gi;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;

import static com.example.timing.utils.RatesGenerator.generateRatesAsc;
import static com.example.timing.utils.RatesGenerator.generateRatesDesc;
import static com.example.timing.utils.RatesGenerator.generateRatesEqual;
import static java.time.Month.JANUARY;
import static org.assertj.core.api.Assertions.assertThat;

public class RatesIndicatorTests {

    private static final LocalDate DATE = LocalDate.of(2020, JANUARY, 1);

    @Test
    public void shouldReturnAPointBecauseRateDecreased() {
        MonthlyRatesIndicator exchangeRatesIndicator = new MonthlyRatesIndicator(generateRatesDesc());

        PartialIndicatorResult result = exchangeRatesIndicator.indicate(DATE);

        assertThat(result).isEqualTo(createGiResult(1.0, 2.0, 1));
    }

    @Test
    public void shouldNotReturnAPointBecauseRateIncreased() {
        MonthlyRatesIndicator exchangeRatesIndicator = new MonthlyRatesIndicator(generateRatesAsc());

        PartialIndicatorResult result = exchangeRatesIndicator.indicate(DATE);

        assertThat(result).isEqualTo(createGiResult(1.0, 0.1, 0));
    }

    @Test
    public void shouldNotReturnAPointBecauseRatesAreEqual() {
        MonthlyRatesIndicator exchangeRatesIndicator = new MonthlyRatesIndicator(generateRatesEqual());

        PartialIndicatorResult result = exchangeRatesIndicator.indicate(DATE);

        assertThat(result).isEqualTo(createGiResult(1.11, 1.11, 0));
    }

    private static PartialIndicatorResult createGiResult(double rate, double comparativeRate, int point) {
        return PartialIndicatorResult.builder()
                .date(DATE)
                .rate(rate)
                .comparativeDate(DATE.minusYears(1))
                .comparativeRate(comparativeRate)
                .point(point)
                .build();
    }
}
