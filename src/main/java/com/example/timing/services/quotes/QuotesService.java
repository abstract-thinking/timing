package com.example.timing.services.quotes;

import com.example.timing.control.rsl.Indices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.stream.Collectors.toList;
import static yahoofinance.histquotes.Interval.WEEKLY;

@Slf4j
@Service
public class QuotesService {

    public Map<Indices, List<HistoryQuote>> fetchQuotes(String[] symbols, LocalDate fromDate, LocalDate toDate) {
        Calendar from = GregorianCalendar.from(fromDate.atStartOfDay(ZoneId.systemDefault()));
        Calendar to = GregorianCalendar.from(toDate.atStartOfDay(ZoneId.systemDefault()));

        List<CompletableFuture<List<HistoryQuote>>> futures = Arrays.stream(symbols)
                .map(symbol ->
                        completedFuture(fetchSymbol(symbol, from, to)).thenApplyAsync(this::mapQuotes))
                .collect(toList());

        return futures.stream()
                .map(CompletableFuture::join)
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(HistoryQuote::getDate).reversed())
                .collect(Collectors.groupingBy(HistoryQuote::getSymbol));
    }

    @Async
    private Stock fetchSymbol(String symbol, Calendar from, Calendar to) {
        try {
            return YahooFinance.get(symbol, from, to, WEEKLY);
        } catch (IOException e) {
            log.error("Fetching of symbol {} failed!", symbol, e);
            throw new RuntimeException("Fetching of symbol " + symbol + " failed!");
        }
    }

    @Async
    private List<HistoryQuote> mapQuotes(Stock stock) {
        List<HistoricalQuote> history;
        try {
            history = stock.getHistory();
        } catch (IOException e) {
            log.error("Mapping of quotes {} failed!", stock, e);
            throw new RuntimeException("Fetching of quotes " + stock + " failed!");
        }

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

        return quotes;
    }

    private LocalDate toLocalDate(Calendar calendar) {
        return LocalDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId()).toLocalDate();
    }
}