package com.example.timing.services.quotes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import static yahoofinance.histquotes.Interval.WEEKLY;

@Slf4j
@Service
public class QuotesService {

    public List<SingleRslResult> fetchAll() {
        Calendar today = Calendar.getInstance();
        Calendar from = (Calendar) today.clone();
        from.add(Calendar.YEAR, -1);

        List<SingleRslResult> rslResults = new ArrayList<>();

        // TODO: Logic should one layer above
        try {
            Map<String, Stock> result = YahooFinance.get(getAllSymbols(), from, today, WEEKLY);

            Collection<Stock> stocks = result.values();
            for (Stock stock : stocks) {
                List<HistoricalQuote> history = stock.getHistory();
                history.sort(Comparator.comparing(HistoricalQuote::getDate).reversed());

                for (int i = 0; i < history.size() - 27; ++i) {
                    final HistoricalQuote historicalQuote = history.get(i);
                    if (historicalQuote.getClose() == null) {
                        log.warn("Data incomplete: " + historicalQuote);
                        continue;
                    }

                    Double rsl = historicalQuote.getClose().doubleValue() / calculateAverage(history);

                    LocalDate localDate = toLocalDate(historicalQuote.getDate());
                    rslResults.add(new SingleRslResult(localDate, stock.getSymbol(), rsl));
                }
            }

        } catch (IOException ex) {
            log.error("Could not fetch data!", ex);
        }

        return rslResults;
    }

    private LocalDate toLocalDate(Calendar calendar) {
        return LocalDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId()).toLocalDate();
    }

    private double calculateAverage(List<HistoricalQuote> history) {
        return history.stream()
                .filter(hq -> hq.getClose() != null)
                .limit(27)
                .mapToDouble(hq -> hq.getClose().doubleValue())
                .average()
                .orElse(Double.NaN);
    }

    private String[] getAllSymbols() {
        return EnumSet.allOf(Indices.class).stream().map(Indices::getSymbol).toArray(String[]::new);
    }
}