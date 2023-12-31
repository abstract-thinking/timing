package com.example.timing.services.rates;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.Month.JANUARY;
import static java.time.Month.SEPTEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.util.StreamUtils.copyToString;

public class RateCsvParserTests {

    @Test
    public void shouldParseInterest() throws IOException {
        String content = readFile("interest.csv");

        Map<LocalDate, Double> result = RateCsvParser.parseInterestRates(content);

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(38);
        assertThat(result.get(LocalDate.of(2019, SEPTEMBER, 18))).isEqualTo(0.00);
        assertThat(result.get(LocalDate.of(1999, JANUARY, 1))).isEqualTo(3.00);
    }

    @Test
    public void shouldParseExchange() throws IOException {
        String content = readFile("exchange.csv");

        Map<YearMonth, Double> result = RateCsvParser.parseMonthlyExchangeRates(content);

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(300);
        assertThat(result.get(YearMonth.of(2020, JANUARY))).isEqualTo(1.1052);
        assertThat(result.get(YearMonth.of(1999, JANUARY))).isEqualTo(1.1384);
    }

    @Test
    public void shouldParseInflation() throws IOException {
        String content = readFile("inflation.csv");

        Map<YearMonth, Double> result = RateCsvParser.parseInflationRates(content);

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(323);
        assertThat(result.get(YearMonth.of(2020, JANUARY))).isEqualTo(1.4);
        assertThat(result.get(YearMonth.of(1997, JANUARY))).isEqualTo(2.0);
    }

    private static String readFile(String fileName) throws IOException {
        return copyToString(new ClassPathResource("data/" + fileName).getInputStream(), UTF_8);
    }
}