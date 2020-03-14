package com.example.timing.services.rates;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static java.time.Month.JANUARY;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RatesServiceIT {

    @Autowired
    private RatesService ratesService;

    @Test
    public void shouldFetchExchangeRates() throws ExecutionException, InterruptedException {
        Map<YearMonth, Double> exchangeRates = ratesService.fetchExchangeRates().get();

        assertThat(exchangeRates).isNotEmpty().hasSizeGreaterThan(253);
        assertThat(exchangeRates.get(YearMonth.of(1999, JANUARY))).isEqualTo(1.1384);
    }

    @Test
    public void shouldFetchInflationRates() throws ExecutionException, InterruptedException {
        Map<YearMonth, Double> inflationRates = ratesService.fetchInflationRates().get();

        assertThat(inflationRates).isNotEmpty().hasSizeGreaterThan(277);
        assertThat(inflationRates.get(YearMonth.of(1997, JANUARY))).isEqualTo(2.0);
    }

    @Test
    public void shouldFetchInterestRates() throws ExecutionException, InterruptedException {
        Map<LocalDate, Double> interestRates = ratesService.fetchInterestRates().get();

        assertThat(interestRates).isNotEmpty().hasSizeGreaterThan(27);
        assertThat(interestRates.get(LocalDate.of(1999, JANUARY, 1))).isEqualTo(3.0);
    }
}

