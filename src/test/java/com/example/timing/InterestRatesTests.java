package com.example.timing;

import org.junit.jupiter.api.Test;

import java.time.Month;
import java.time.YearMonth;

import static com.example.timing.RatesGenerator.generateInterestRatesAsc;
import static com.example.timing.RatesGenerator.generateInterestRatesDesc;
import static org.assertj.core.api.Assertions.assertThat;

public class InterestRatesTests {

    @Test
    public void shouldReturnAPointBecauseRateDecreased() {
        InterestRates interestRates = new InterestRates(generateInterestRatesDesc());

        PartialIndicatorResult result = interestRates.calculate(YearMonth.now());

        assertThat(result).isEqualTo(createGiResult(0.0, Month.SEPTEMBER, 1));
    }

    @Test
    public void shouldNotReturnAPointBecauseRateIncreased() {
        InterestRates interestRates = new InterestRates(generateInterestRatesAsc());

        PartialIndicatorResult result = interestRates.calculate(YearMonth.now());

        assertThat(result).isEqualTo(createGiResult(0.1, Month.MAY, 0));
    }

    private PartialIndicatorResult createGiResult(double rate, Month month, int point) {
        return PartialIndicatorResult.builder()
                .date(YearMonth.of(2019, Month.SEPTEMBER))
                .rate(rate)
                .comparativeDate(YearMonth.of(2015, month))
                .comparativeRate(0.05)
                .point(point)
                .build();
    }
}
