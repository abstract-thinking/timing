package com.example.timing.control.gi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static java.time.Month.MAY;
import static java.time.Month.NOVEMBER;
import static org.assertj.core.api.Assertions.assertThat;

public class SeasonIndicatorTests {

    private SeasonIndicator seasonIndicator;

    @BeforeEach
    public void setUp() {
        seasonIndicator = new SeasonIndicator();
    }

    @Test
    public void shouldBeAFriendlyMonth() {
        LocalDate date = LocalDate.of(2020, NOVEMBER, 1);
        PartialIndicatorResult result = seasonIndicator.indicate(date);

        assertThat(result).isEqualTo(createPartialIndicatorResult(date, 1));
    }

    @Test
    public void shouldNotBeAFriendlyMonth() {
        LocalDate date = LocalDate.of(2020, MAY, 1);
        PartialIndicatorResult result = seasonIndicator.indicate(date);

        assertThat(result).isEqualTo(createPartialIndicatorResult(date, 0));
    }

    private static PartialIndicatorResult createPartialIndicatorResult(LocalDate date, int point) {
        return PartialIndicatorResult.builder()
                .date(date)
                .rate(0.0)
                .comparativeDate(date)
                .comparativeRate(0.0)
                .point(point)
                .build();
    }
}
