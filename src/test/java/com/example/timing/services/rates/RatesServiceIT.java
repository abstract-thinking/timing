package com.example.timing.services.rates;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

import static java.time.Month.JANUARY;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RatesServiceIT {

    @Autowired
    private RatesService ratesService;

    @Test
    public void shouldFetchExchangeRates() {
        Map<YearMonth, Double> exchangeRates = ratesService.fetchExchangeRates();

        assertThat(exchangeRates).isNotEmpty().hasSizeGreaterThan(253);
        assertThat(exchangeRates.get(YearMonth.of(1999, JANUARY))).isEqualTo(1.1384);
    }

    @Test
    public void shouldFetchInflationRates() {
        Map<YearMonth, Double> inflationRates = ratesService.fetchInflationRates();

        assertThat(inflationRates).isNotEmpty().hasSizeGreaterThan(277);
        assertThat(inflationRates.get(YearMonth.of(1997, JANUARY))).isEqualTo(2.0);
    }

    @Test
    public void shouldFetchInterestRates() {
        Map<LocalDate, Double> interestRates = ratesService.fetchInterestRates();

        assertThat(interestRates).isNotEmpty().hasSizeGreaterThan(27);
        assertThat(interestRates.get(LocalDate.of(1999, JANUARY, 1))).isEqualTo(3.0);
    }
}

