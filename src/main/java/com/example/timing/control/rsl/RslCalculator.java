package com.example.timing.control.rsl;

import com.example.timing.boundary.rsl.CumulateRslResult;
import com.example.timing.services.quotes.HistoryQuote;
import com.example.timing.services.quotes.QuotesService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;
import static java.util.Collections.singletonList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class RslCalculator {

    private static final int ONE_YEAR_BEFORE = -1;

    private static final int PAST_WEEKS = 27;

    private final QuotesService service;

    @Setter
    private LocalDate toDate;

    @Setter
    private LocalDate fromDate;

    public RslCalculator(QuotesService service) {
        this.service = service;

        toDate = LocalDate.now();
        fromDate = toDate.minusYears(1);
    }

    public List<CumulateRslResult> calculate(String name) {
        List<String> symbol = singletonList(Indices.valueOf(name.toUpperCase()).getSymbol());
        return doCalculation(symbol);
    }

    public List<CumulateRslResult> calculate() {
        return doCalculation(allMajorIndices());
    }

    private List<String> allMajorIndices() {
        return EnumSet.allOf(Indices.class).stream()
                .filter(Indices::isMajor)
                .map(Indices::getSymbol)
                .collect(toList());
    }

    private List<CumulateRslResult> doCalculation(List<String> symbols) {
        Map<Indices, List<HistoryQuote>> completeHistoricalQuotes = service.fetchQuotes(symbols, fromDate, toDate);

        List<RslResult> rslResults = calculateRslForEachSymbol(completeHistoricalQuotes);
        List<CumulateRslResult> cumulateRslResults = cumulateRsl(rslResults);

        cumulateRslResults.sort(comparing(CumulateRslResult::getBegin).reversed());

        return cumulateRslResults;
    }

    private List<RslResult> calculateRslForEachSymbol(Map<Indices, List<HistoryQuote>> completeHistoricalQuotes) {
        List<RslResult> rslResults = new ArrayList<>();
        for (Map.Entry<Indices, List<HistoryQuote>> historicalQuotes : completeHistoricalQuotes.entrySet()) {
            List<HistoryQuote> historicalQuotesBySymbol = historicalQuotes.getValue();
            if (historicalQuotesBySymbol.size() < PAST_WEEKS) {
                log.info("Too less entries {} for {}", historicalQuotesBySymbol.size(), historicalQuotes.getKey());
                continue;
            }

            for (int i = 0; i < historicalQuotesBySymbol.size() - PAST_WEEKS; ++i) {
                HistoryQuote historyQuote = historicalQuotesBySymbol.get(i);
                Double rsl = calculateRsl(historyQuote, historicalQuotesBySymbol.subList(i, historicalQuotesBySymbol.size()));
                RslResult result = new RslResult(historyQuote.getSymbol(), historyQuote.getDate(), rsl);
                log.info("RSL calculated {}", result);
                rslResults.add(result);
            }
        }

        return rslResults;
    }

    private double calculateRsl(HistoryQuote historyQuote, List<HistoryQuote> historicalQuotes) {
        return historyQuote.getAdjClose().doubleValue() / calculateAverage(historicalQuotes);
    }

    private double calculateAverage(List<HistoryQuote> historicalQuotes) {
        return historicalQuotes.stream()
                .filter(hq -> hq.getAdjClose() != null)
                .limit(PAST_WEEKS)
                .mapToDouble(hq -> hq.getAdjClose().doubleValue())
                .average()
                .orElse(Double.NaN);
    }

    private List<CumulateRslResult> cumulateRsl(List<RslResult> rslResults) {
        List<CumulateRslResult> cumulateRslResults = new ArrayList<>();

        final LocalDate endDate = toDate.minusMonths(6);
        for (LocalDate date = toDate; date.isAfter(endDate); date = date.minusWeeks(1)) {
            LocalDate begin = date.with(previousOrSame(MONDAY));
            LocalDate end = date.with(nextOrSame(SUNDAY));

            int divisor = 0;
            double sum = 0.0;
            for (RslResult result : rslResults) {
                LocalDate resultDate = result.getDate();

//                // First condition: Take all values which are not on a Monday
//                if (resultDate.isAfter(begin) && resultDate.isBefore(end) || resultDate.equals(end)) {
//                    sum += result.getRsl();
//                    ++divisor;
//                    log.info("MATCH: {}", result);
//                }
//                // Second condition: Take the values from Monday before
//                else if (resultDate.equals(begin.plusWeeks(1))){
//                    sum += result.getRsl();
//                    ++divisor;
//                    log.info("MONDAY MATCH: {}", result);
//                }

                // I can not really interpret the data of Yahoo. I skip the data inside the week
                // and use the MONDAY result for the complete week.
                if (resultDate.equals(begin)) {
                    sum += result.getRsl();
                    ++divisor;
                    log.info("MONDAY MATCH: {}", result);
                }

            }

            double average = sum / divisor;

            CumulateRslResult result = new CumulateRslResult(begin, end.isAfter(toDate) ? toDate : end, average);
            log.info("Complete RSL calculated {}", result);
            cumulateRslResults.add(result);
        }

        return cumulateRslResults;
    }
}
