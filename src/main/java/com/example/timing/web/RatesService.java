package com.example.timing.web;

import com.example.timing.utils.RateCsvParser;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

import static com.example.timing.utils.RateCsvParser.parseInterestRates;
import static com.example.timing.utils.RateCsvParser.parseRates;
import static com.example.timing.web.SeriesKey.EXCHANGE;
import static com.example.timing.web.SeriesKey.INFLATION;
import static com.example.timing.web.SeriesKey.INTEREST;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.util.StreamUtils.copyToString;

@Service
public class RatesService {

    // https://sdw.ecb.int/quickviewexport.do?type=csv&SERIES_KEY=143.FM.B.U2.EUR.4F.KR.MRR_FR.LEV
    // https://sdw.ecb.int/quickviewexport.do?type=csv&SERIES_KEY=122.ICP.M.U2.N.000000.4.ANR
    // https://sdw.ecb.int/quickviewexport.do?type=csv&SERIES_KEY=120.EXR.M.USD.EUR.SP00.E
    private static final String RESOURCE_URL = "https://sdw.ecb.int/quickviewexport.do?type=csv&SERIES_KEY={key}";

    public Map<LocalDate, Double> fetchInterestRates() {
        return parseInterestRates(fetchRates(INTEREST));
    }

    public Map<YearMonth, Double> fetchInflationRates() {
        return parseRates(fetchRates(INFLATION));
    }

    public Map<YearMonth, Double> fetchExchangeRates() {
        return parseRates(fetchRates(EXCHANGE));
    }

    private String fetchRates(SeriesKey key) {
        return new RestTemplate().getForEntity(RESOURCE_URL, String.class, key.getKey()).getBody();
    }

    public Map<LocalDate, Double> readInterestRates() throws IOException {
        return RateCsvParser.parseInterestRates(readRates(), 1);
    }

    public Map<YearMonth, Double> readInflationRates() throws IOException {
        return parseRates(readRates(), 2);
    }

    public Map<YearMonth, Double> readExchangeRates() throws IOException {
        return parseRates(readRates(), 3);
    }

    private String readRates() throws IOException {
        return copyToString(new ClassPathResource("gi-history.csv").getInputStream(), UTF_8);
    }

}