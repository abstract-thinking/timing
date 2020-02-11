package com.example.timing;

import org.junit.jupiter.api.Test;

import java.time.Month;
import java.time.YearMonth;

import static org.assertj.core.api.Assertions.assertThat;

public class SeasonTests {

    @Test
    public void shouldBeFriendlyMonth() {
        Season season = new Season();

        GiPartResult result = season.calculate(YearMonth.of(2020, Month.NOVEMBER));

        GiPartResult expectedResult = GiPartResult.builder()
                .date(YearMonth.of(2020, Month.NOVEMBER))
                .rate(0.0)
                .comparativeDate(YearMonth.of(2020, Month.NOVEMBER))
                .comparativeRate(0.0)
                .point(1)
                .build();

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    public void shouldNotBeAFriendlyMonth() {
        Season season = new Season();

        GiPartResult result = season.calculate(YearMonth.of(2020, Month.MAY));

        GiPartResult expectedResult = GiPartResult.builder()
                .date(YearMonth.of(2020, Month.MAY))
                .rate(0.0)
                .comparativeDate(YearMonth.of(2020, Month.MAY))
                .comparativeRate(0.0)
                .point(0)
                .build();

        assertThat(result).isEqualTo(expectedResult);
    }

}
