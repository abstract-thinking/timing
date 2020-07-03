package com.example.timing.services.rates;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.example.timing.services.rates.RateCsvParser.parseInterestRates;
import static com.example.timing.services.rates.RateCsvParser.parseRates;
import static com.example.timing.services.rates.SeriesKey.EXCHANGE;
import static com.example.timing.services.rates.SeriesKey.INFLATION;
import static com.example.timing.services.rates.SeriesKey.INTEREST;
import static java.util.concurrent.CompletableFuture.completedFuture;

@Service
public class RatesService {

    // https://sdw.ecb.int/quickviewexport.do?type=csv&SERIES_KEY=143.FM.B.U2.EUR.4F.KR.MRR_FR.LEV
    // https://sdw.ecb.int/quickviewexport.do?type=csv&SERIES_KEY=122.ICP.M.U2.N.000000.4.ANR
    // https://sdw.ecb.int/quickviewexport.do?type=csv&SERIES_KEY=120.EXR.M.USD.EUR.SP00.E
    private final String template_url;

    public RatesService(RatesServerConfiguration config) {
        this.template_url = config.getUrl().concat("/quickviewexport.do?type=csv&SERIES_KEY={key}");
    }

    @Async
    public CompletableFuture<Map<LocalDate, Double>> fetchInterestRates() {
        return completedFuture(parseInterestRates(fetchRates(INTEREST)));
    }

    @Async
    public CompletableFuture<Map<YearMonth, Double>> fetchInflationRates() {
        return completedFuture(parseRates(fetchRates(INFLATION)));
    }

    @Async
    public CompletableFuture<Map<YearMonth, Double>> fetchExchangeRates() {
        return completedFuture(parseRates(fetchRates(EXCHANGE)));
    }

    private String fetchRates(SeriesKey key) {
        return new RestTemplate().getForEntity(template_url, String.class, key.getKey()).getBody();
    }

    @Data
    @Configuration
    @ConfigurationProperties(prefix = "rates.server")
    static class RatesServerConfiguration {

        @NotBlank(message = "rates.server.url must be set in the configuration")
        private String url;
    }
}