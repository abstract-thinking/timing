package com.example.timing.utils;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.util.StreamUtils.copyToString;

public class RateCsvParserTests {

    @Test
    public void shouldParseInterest() throws IOException {
        String content = readFile("interest.csv");

        Map<LocalDate, Double> result = RateCsvParser.parseInterest(content);

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(28);
        assertThat(result.get(LocalDate.of(2019, Month.SEPTEMBER, 18))).isEqualTo(0.00);
        assertThat(result.get(LocalDate.of(1999, Month.JANUARY, 1))).isEqualTo(3.00);
    }

    @Test
    public void shouldParseExchange() throws IOException {
        String content = readFile("exchange.csv");

        Map<YearMonth, Double> result = RateCsvParser.parseRates(content);

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(253);
        assertThat(result.get(YearMonth.of(2020, Month.JANUARY))).isEqualTo(1.1052);
        assertThat(result.get(YearMonth.of(1999, Month.JANUARY))).isEqualTo(1.1384);
    }

    @Test
    public void shouldParseInflation() throws IOException {
        String content = readFile("inflation.csv");

        Map<YearMonth, Double> result = RateCsvParser.parseRates(content);

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(277);
        assertThat(result.get(YearMonth.of(2020, Month.JANUARY))).isEqualTo(1.4);
        assertThat(result.get(YearMonth.of(1997, Month.JANUARY))).isEqualTo(2.0);
    }

    private String readFile(String fileName) throws IOException {
        return copyToString(new ClassPathResource("data/" + fileName).getInputStream(), UTF_8);
    }
}