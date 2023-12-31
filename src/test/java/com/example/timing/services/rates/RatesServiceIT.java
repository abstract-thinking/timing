package com.example.timing.services.rates;

import com.example.timing.control.gi.domain.DailyUSDollarEuroInterestRate;
import com.example.timing.control.gi.domain.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClientException;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static java.time.Month.JANUARY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class RatesServiceIT {

    @Autowired
    private RatesService ratesService;

    @Test
    public void shouldFetchExchangeRatesMonthly() throws ExecutionException, InterruptedException {
        Map<YearMonth, Double> exchangeRates = ratesService.fetchExchangeRatesMonthly().get();

        assertThat(exchangeRates).isNotEmpty().hasSizeGreaterThan(253);
        assertThat(exchangeRates.get(YearMonth.of(1999, JANUARY))).isEqualTo(1.1384);
    }

    @Test
    public void shouldFetchExchangeRatesDaily() throws ExecutionException, InterruptedException {
        Map<LocalDate, DailyUSDollarEuroInterestRate> exchangeRates = ratesService.fetchExchangeRatesDaily().get();

        assertThat(exchangeRates).isNotEmpty().hasSizeGreaterThan(253);
        assertThat(exchangeRates.get(LocalDate.of(1999, JANUARY, 4))).isEqualTo(
                new DailyUSDollarEuroInterestRate(1.1789, Status.NORMAL));
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

    @Test
    public void shouldThrowIfNotSuccessful() {
        ratesService = new RatesService(createConfigurationWithWrongServerUrl());

        assertThatThrownBy(() -> ratesService.fetchInterestRates().get(), "check NotFound exception")
                .isInstanceOf(RestClientException.class);
    }

    private RatesService.RatesServerConfiguration createConfigurationWithWrongServerUrl() {
        RatesService.RatesServerConfiguration configuration = new RatesService.RatesServerConfiguration();
        configuration.setTemplateUrl("https://data-api.ecb.europa.invalid/service/data/{path}?format=csvdata");
        configuration.setFm("FM/B.U2.EUR.4F.KR.MRR_FR.LEV");
        return configuration;
    }
}

