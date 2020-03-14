package com.example.timing.services.quotes;

import com.example.timing.control.rsl.Indices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static yahoofinance.histquotes.Interval.WEEKLY;

@Slf4j
@Service
public class QuotesService {

    public Map<Indices, List<HistoryQuote>> fetchQuotes(String[] symbols, LocalDate fromDate, LocalDate toDate) {
        try {
            Calendar from = GregorianCalendar.from(fromDate.atStartOfDay(ZoneId.systemDefault()));
            Calendar to = GregorianCalendar.from(toDate.atStartOfDay(ZoneId.systemDefault()));

            Map<String, Stock> result = YahooFinance.get(symbols, from, to, WEEKLY);

            Map<Indices, List<HistoryQuote>> results = new HashMap<>();
            Collection<Stock> stocks = result.values();
            for (Stock stock : stocks) {
                List<HistoricalQuote> history = stock.getHistory();
                history.sort(Comparator.comparing(HistoricalQuote::getDate).reversed());

                List<HistoryQuote> quotes = new ArrayList<>();
                for (final HistoricalQuote historicalQuote : history) {
                    if (historicalQuote.getAdjClose() == null) {
                        log.warn("Data incomplete: " + historicalQuote);
                        continue;
                    }

                    quotes.add(new HistoryQuote(
                            Indices.from(historicalQuote.getSymbol()),
                            toLocalDate(historicalQuote.getDate()),
                            historicalQuote.getAdjClose()));
                }

                results.put(Indices.from(stock.getSymbol()), quotes);
            }

            return results;
        } catch (IOException ex) {
            log.error("Could not fetch data!", ex);
            throw new RuntimeException("Failed to fetch data!");
        }
    }

    private LocalDate toLocalDate(Calendar calendar) {
        return LocalDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId()).toLocalDate();
    }
}