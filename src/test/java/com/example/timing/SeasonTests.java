package com.example.timing;

import org.junit.jupiter.api.Test;

import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

public class SeasonTests {

    @Test
    public void shouldBeFriendlyMonth() {
        Season season = new Season(Month.DECEMBER);

        assertThat(season.asString()).isEqualTo("December");
        assertThat(season.getPoint()).isEqualTo(1);
    }

    @Test
    public void shouldNotBeAFriendlyMonth() {
        Season season = new Season(Month.MAY);

        assertThat(season.asString()).isEqualTo("May");
        assertThat(season.getPoint()).isEqualTo(0);
    }

}
