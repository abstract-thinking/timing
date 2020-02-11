package com.example.timing;

import org.junit.jupiter.api.Test;

import java.time.Month;
import java.time.YearMonth;

import static com.example.timing.RatesGenerator.generateRatesAsc;
import static com.example.timing.RatesGenerator.generateRatesDesc;
import static com.example.timing.RatesGenerator.generateRatesEqual;
import static org.assertj.core.api.Assertions.assertThat;

public class RatesTests {

    private static final YearMonth DATE = YearMonth.of(2020, Month.JANUARY);

    @Test
    public void shouldReturnAPointBecauseRateDecreased() {
        Rates exchangeRates = new Rates(generateRatesDesc());

        PartialIndicatorResult result = exchangeRates.calculate(DATE);

        assertThat(result).isEqualTo(createGiResult(1.0, 2.0, 1));
    }

    @Test
    public void shouldNotReturnAPointBecauseRateIncreased() {
        Rates exchangeRates = new Rates(generateRatesAsc());

        PartialIndicatorResult result = exchangeRates.calculate(DATE);

        assertThat(result).isEqualTo(createGiResult(1.0, 0.1, 0));
    }

    @Test
    public void shouldNotReturnAPointBecauseRatesAreEqual() {
        Rates exchangeRates = new Rates(generateRatesEqual());

        PartialIndicatorResult result = exchangeRates.calculate(DATE);

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
