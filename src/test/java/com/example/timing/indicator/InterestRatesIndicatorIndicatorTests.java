package com.example.timing.indicator;

import com.example.timing.results.PartialIndicatorResult;
import org.junit.jupiter.api.Test;

import java.time.Month;
import java.time.YearMonth;

import static com.example.timing.RatesGenerator.generateInterestRatesAsc;
import static com.example.timing.RatesGenerator.generateInterestRatesDesc;
import static org.assertj.core.api.Assertions.assertThat;

public class InterestRatesIndicatorIndicatorTests {

    @Test
    public void shouldReturnAPointBecauseRateDecreased() {
        InterestRatesIndicator interestRatesIndicator = new InterestRatesIndicator(generateInterestRatesDesc());

        PartialIndicatorResult result = interestRatesIndicator.indicate(YearMonth.now());

        assertThat(result).isEqualTo(createGiResult(0.0, Month.SEPTEMBER, 1));
    }

    @Test
    public void shouldNotReturnAPointBecauseRateIncreased() {
        InterestRatesIndicator interestRatesIndicator = new InterestRatesIndicator(generateInterestRatesAsc());

        PartialIndicatorResult result = interestRatesIndicator.indicate(YearMonth.now());

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
