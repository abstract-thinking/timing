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
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static yahoofinance.histquotes.Interval.WEEKLY;

@Slf4j
@Service
public class QuotesService {

    public Map<Indices, List<HistoryQuote>> fetchQuotes(List<String> symbols, LocalDate fromDate, LocalDate toDate) {
        Calendar from = GregorianCalendar.from(fromDate.atStartOfDay(ZoneId.systemDefault()));
        Calendar to = GregorianCalendar.from(toDate.atStartOfDay(ZoneId.systemDefault()));

        List<CompletableFuture<List<HistoryQuote>>> futures = symbols.stream()
                .map(symbol ->
                        completedFuture(fetchSymbol(symbol, from, to))
                                .thenApplyAsync(this::mapQuotes))
                .collect(toList());

        return futures.stream()
                .map(CompletableFuture::join)
                .flatMap(Collection::stream)
                .sorted(comparing(HistoryQuote::getDate).reversed())
                .collect(groupingBy(HistoryQuote::getSymbol));
    }

    @Async
    private Optional<Stock> fetchSymbol(String symbol, Calendar from, Calendar to) {
        try {
            return Optional.ofNullable(YahooFinance.get(symbol, from, to, WEEKLY));
        } catch (IOException e) {
            log.error("Fetching of symbol {} failed!", symbol, e);
            return Optional.empty();
        }
    }

    @Async
    private List<HistoryQuote> mapQuotes(Optional<Stock> stock) {
        if (stock.isEmpty()) {
            return emptyList();
        }

        List<HistoricalQuote> history;
        try {
            history = stock.get().getHistory();
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