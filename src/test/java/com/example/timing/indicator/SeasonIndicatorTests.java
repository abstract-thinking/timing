package com.example.timing.indicator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Month;
import java.time.YearMonth;

import static org.assertj.core.api.Assertions.assertThat;

public class SeasonIndicatorTests {

    private SeasonIndicator seasonIndicator;

    @BeforeEach
    public void setUp() {
        seasonIndicator = new SeasonIndicator();
    }

    @Test
    public void shouldBeAFriendlyMonth() {
        PartialIndicatorResult result = seasonIndicator.indicate(YearMonth.of(2020, Month.NOVEMBER));

        assertThat(result).isEqualTo(createPartialIndicatorResult(Month.NOVEMBER, 1));
    }

    @Test
    public void shouldNotBeAFriendlyMonth() {
        PartialIndicatorResult result = seasonIndicator.indicate(YearMonth.of(2020, Month.MAY));

        assertThat(result).isEqualTo(createPartialIndicatorResult(Month.MAY, 0));
    }

    private PartialIndicatorResult createPartialIndicatorResult(Month month, int point) {
        return PartialIndicatorResult.builder()
                .date(YearMonth.of(2020, month))
                .rate(0.0)
                .comparativeDate(YearMonth.of(2020, month))
                .comparativeRate(0.0)
                .point(point)
                .build();
    }
}
