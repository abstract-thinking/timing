package com.example.timing.services.rates;

import com.example.timing.control.gi.domain.DailyUSDollarEuroInterestRate;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.example.timing.services.rates.RateCsvParser.parseDailyExchangeRates;
import static com.example.timing.services.rates.RateCsvParser.parseInflationRates;
import static com.example.timing.services.rates.RateCsvParser.parseInterestRates;
import static com.example.timing.services.rates.RateCsvParser.parseMonthlyExchangeRates;
import static java.util.concurrent.CompletableFuture.completedFuture;

@Slf4j
@Service
public class RatesService {

    private final RatesServerConfiguration configuration;

    // Exchange rates
    // https://data-api.ecb.europa.eu/service/data/EXR/M.USD.EUR.SP00.E?format=csvdata
    // https://data-api.ecb.europa.eu/service/data/EXR/D.USD.EUR.SP00.A?format=csvdata
    // Financial market
    // https://data-api.ecb.europa.eu/service/data/FM/B.U2.EUR.4F.KR.MRR_FR.LEV?format=csvdata
    // Indices of Consumer Prices
    // https://data-api.ecb.europa.eu/service/data/ICP/M.U2.N.000000.4.ANR?format=csvdata


    public RatesService(RatesServerConfiguration config) {
        this.configuration = config;
    }

    @Async
    public CompletableFuture<Map<LocalDate, Double>> fetchInterestRates() {
        return completedFuture(parseInterestRates(fetchRates(configuration.getFm())));
    }

    @Async
    public CompletableFuture<Map<YearMonth, Double>> fetchInflationRates() {
        return completedFuture(parseInflationRates(fetchRates(configuration.getHicp())));
    }

    @Async
    public CompletableFuture<Map<YearMonth, Double>> fetchExchangeRatesMonthly() {
        return completedFuture(parseMonthlyExchangeRates(fetchRates(configuration.getExrMonthly())));
    }

    @Async
    public CompletableFuture<Map<LocalDate, DailyUSDollarEuroInterestRate>> fetchExchangeRatesDaily() {
        return completedFuture(parseDailyExchangeRates(fetchRates(configuration.getExrDaily())));
    }

    private String fetchRates(String path) {
        String url = configuration.getTemplateUrl().replace("{path}", path);
        return new RestTemplate().getForEntity(url, String.class).getBody();
    }

    @Data
    @Configuration
    @ConfigurationProperties(prefix = "data")
    public static class RatesServerConfiguration {

        @NotBlank(message = "data.template_url must be set in the configuration")
        private String templateUrl;

        @NotBlank(message = "data.exr_monthly must be set in the configuration")
        private String exrMonthly;

        @NotBlank(message = "data.exr_daily must be set in the configuration")
        private String exrDaily;

        @NotBlank(message = "data.fm must be set in the configuration")
        private String fm;

        @NotBlank(message = "data.hicp must be set in the configuration")
        private String hicp;
    }
}