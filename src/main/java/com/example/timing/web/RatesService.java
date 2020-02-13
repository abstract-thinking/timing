package com.example.timing.web;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

import static com.example.timing.utils.RateCsvParser.parseInterest;
import static com.example.timing.utils.RateCsvParser.parseRates;
import static com.example.timing.web.SeriesKey.EXCHANGE;
import static com.example.timing.web.SeriesKey.INFLATION;
import static com.example.timing.web.SeriesKey.INTEREST;

@Service
public class RatesService {

    // https://sdw.ecb.int/quickviewexport.do?type=csv&SERIES_KEY=143.FM.B.U2.EUR.4F.KR.MRR_FR.LEV
    // https://sdw.ecb.int/quickviewexport.do?type=csv&SERIES_KEY=122.ICP.M.U2.N.000000.4.ANR
    // https://sdw.ecb.int/quickviewexport.do?type=csv&SERIES_KEY=120.EXR.M.USD.EUR.SP00.E
    private static final String RESOURCE_URL = "https://sdw.ecb.int/quickviewexport.do?type=csv&SERIES_KEY={key}";

    public Map<LocalDate, Double> processInterestRates() {
        return parseInterest(fetchRates(INTEREST));
    }

    public Map<YearMonth, Double> processInflationRates() {
        return parseRates(fetchRates(INFLATION));
    }

    public Map<YearMonth, Double> processExchangeRates() {
        return parseRates(fetchRates(EXCHANGE));
    }

    private String fetchRates(SeriesKey key) {
        return new RestTemplate().getForEntity(RESOURCE_URL, String.class, key.getKey()).getBody();
    }
}