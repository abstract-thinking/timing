package com.example.timing.services.rates;

import com.example.timing.configuration.EcbConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

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
import static org.springframework.http.HttpStatus.BAD_GATEWAY;

@Slf4j
@Service
public class RatesService {

    // https://sdw.ecb.int/quickviewexport.do?type=csv&SERIES_KEY=143.FM.B.U2.EUR.4F.KR.MRR_FR.LEV
    // https://sdw.ecb.int/quickviewexport.do?type=csv&SERIES_KEY=122.ICP.M.U2.N.000000.4.ANR
    // https://sdw.ecb.int/quickviewexport.do?type=csv&SERIES_KEY=120.EXR.M.USD.EUR.SP00.E
    private final String template_url;

    public RatesService(EcbConfiguration config) {
        this.template_url = config.getUrl().concat("?type=csv&SERIES_KEY={key}");
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
        ResponseEntity<String> entity = new RestTemplate()
                .getForEntity(template_url, String.class, key.getKey());

        if (entity.getStatusCode().is2xxSuccessful()) {
            return entity.getBody();
        }

        log.error("Unexpected response from server: " + entity.getStatusCode());
        throw new ResponseStatusException(BAD_GATEWAY, "Oops, something went wrong!");
    }

}