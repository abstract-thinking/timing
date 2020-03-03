package com.example.timing.control.gi;

import org.junit.jupiter.api.Test;

import java.time.YearMonth;

import static com.example.timing.utils.RatesGenerator.generateRatesAsc;
import static com.example.timing.utils.RatesGenerator.generateRatesDesc;
import static com.example.timing.utils.RatesGenerator.generateRatesEqual;
import static java.time.Month.JANUARY;
import static org.assertj.core.api.Assertions.assertThat;

public class RatesIndicatorTests {

    private static final YearMonth DATE = YearMonth.of(2020, JANUARY);

    @Test
    public void shouldReturnAPointBecauseRateDecreased() {
        RatesIndicator exchangeRatesIndicator = new RatesIndicator(generateRatesDesc());

        PartialIndicatorResult result = exchangeRatesIndicator.indicate(DATE);

        assertThat(result).isEqualTo(createGiResult(1.0, 2.0, 1));
    }

    @Test
    public void shouldNotReturnAPointBecauseRateIncreased() {
        RatesIndicator exchangeRatesIndicator = new RatesIndicator(generateRatesAsc());

        PartialIndicatorResult result = exchangeRatesIndicator.indicate(DATE);

        assertThat(result).isEqualTo(createGiResult(1.0, 0.1, 0));
    }

    @Test
    public void shouldNotReturnAPointBecauseRatesAreEqual() {
        RatesIndicator exchangeRatesIndicator = new RatesIndicator(generateRatesEqual());

        PartialIndicatorResult result = exchangeRatesIndicator.indicate(DATE);

        assertThat(result).isEqualTo(createGiResult(1.11, 1.11, 0));
    }

    private PartialIndicatorResult createGiResult(double rate, double comparativeRate, int point) {
        return PartialIndicatorResult.builder()
                .date(DATE)
                .rate(rate)
                .comparativeDate(DATE.minusYears(1))
                .comparativeRate(comparativeRate)
                .point(point)
                .build();
    }
}
