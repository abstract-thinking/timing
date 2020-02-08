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
    public void shouldReturnRates() {
        Rates exchangeRates = new Rates(generateRatesDesc(), DATE);

        assertThat(exchangeRates.getRate()).isEqualTo(1.0);
        assertThat(exchangeRates.getComparativeRate()).isEqualTo(2.0);
    }


    @Test
    public void shouldDecrease() {
        Rates exchangeRates = new Rates(generateRatesDesc(), DATE);

        assertThat(exchangeRates.isDecreased()).isTrue();
        assertThat(exchangeRates.getPoint()).isEqualTo(1);
    }

    @Test
    public void shouldNotDecrease() {
        Rates exchangeRates = new Rates(generateRatesAsc(), DATE);

        assertThat(exchangeRates.isDecreased()).isFalse();
        assertThat(exchangeRates.getPoint()).isEqualTo(0);
    }

    @Test
    public void shouldNotDecreaseBecauseRatesAreEqual() {
        Rates exchangeRates = new Rates(generateRatesEqual(), DATE);

        assertThat(exchangeRates.isDecreased()).isFalse();
        assertThat(exchangeRates.getPoint()).isEqualTo(0);
    }
}
