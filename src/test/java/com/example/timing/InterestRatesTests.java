package com.example.timing;

import org.junit.jupiter.api.Test;

import static com.example.timing.RatesGenerator.generateInterestRatesAsc;
import static com.example.timing.RatesGenerator.generateInterestRatesDesc;
import static org.assertj.core.api.Assertions.assertThat;

public class InterestRatesTests {

    @Test
    public void shouldDecrease() {
        InterestRates interestRates = new InterestRates(generateInterestRatesDesc());

        assertThat(interestRates.isDecreased()).isTrue();
        assertThat(interestRates.getPoint()).isEqualTo(1);
    }

    @Test
    public void shouldNotDecrease() {
        InterestRates interestRates = new InterestRates(generateInterestRatesAsc());

        assertThat(interestRates.isDecreased()).isFalse();
        assertThat(interestRates.getPoint()).isEqualTo(0);
    }

}
