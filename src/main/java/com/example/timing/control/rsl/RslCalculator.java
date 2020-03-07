package com.example.timing.control.rsl;

import com.example.timing.boundary.rsl.RslResult;
import com.example.timing.services.quotes.HistoryQuote;
import com.example.timing.services.quotes.QuotesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;
import static java.util.Calendar.YEAR;
import static java.util.Comparator.comparing;

@Slf4j
@Service
public class RslCalculator {

    private static final int ONE_YEAR_BEFORE = -1;

    private static final int PAST_WEEKS = 27;

    private final QuotesService service;

    public RslCalculator(QuotesService service) {
        this.service = service;
    }

    public List<RslResult> calculate(String name) {
        String[] symbol = {Indices.valueOf(name.toUpperCase()).getSymbol()};
        return doCalculation(symbol);
    }

    public List<RslResult> calculate() {
        return doCalculation(allMajorIndices());
    }

    private String[] allMajorIndices() {
        return EnumSet.allOf(Indices.class).stream()
                .filter(Indices::isMajor)
                .map(Indices::getSymbol)
                .toArray(String[]::new);
    }

    private List<RslResult> doCalculation(String[] symbols) {
        Calendar today = Calendar.getInstance();
        Calendar from = (Calendar) today.clone();
        from.add(YEAR, ONE_YEAR_BEFORE);

        Map<Indices, List<HistoryQuote>> allHistoricalQuotes = service.fetchQuotes(symbols, from, today);

        List<SingleRslResult> singleRslResults = calculateRsl(allHistoricalQuotes);
        List<RslResult> cumulateRslResults = cumulateRsl(singleRslResults);

        cumulateRslResults.sort(comparing(RslResult::getBegin).reversed());

        return cumulateRslResults;
    }

    private List<SingleRslResult> calculateRsl(Map<Indices, List<HistoryQuote>> allHistoricalQuotes) {
        List<SingleRslResult> singleRslResults = new ArrayList<>();
        for (Map.Entry<Indices, List<HistoryQuote>> entry : allHistoricalQuotes.entrySet()) {
            List<HistoryQuote> historicalQuotes = entry.getValue();
            if (historicalQuotes.size() < PAST_WEEKS + 20) {
                log.info("Too less entries {} for {}", historicalQuotes.size(), entry.getKey());
                continue;
            }

            for (int i = 0; i < historicalQuotes.size() - PAST_WEEKS; ++i) {
                HistoryQuote historyQuote = historicalQuotes.get(i);
                Double rsl = historyQuote.getAdjClose().doubleValue() /
                        calculateAverage(historicalQuotes.subList(i, historicalQuotes.size()));
                SingleRslResult result = new SingleRslResult(historyQuote.getSymbol(), historyQuote.getDate(), rsl);
                log.info("RSL calculated {}", result);
                singleRslResults.add(result);
            }
        }

        return singleRslResults;
    }

    private double calculateAverage(List<HistoryQuote> historicalQuotes) {
        return historicalQuotes.stream()
                .filter(hq -> hq.getAdjClose() != null)
                .limit(PAST_WEEKS)
                .mapToDouble(hq -> hq.getAdjClose().doubleValue())
                .average()
                .orElse(Double.NaN);
    }

    private List<RslResult> cumulateRsl(List<SingleRslResult> singleRslResults) {
        List<RslResult> cumulateRslResult = new ArrayList<>();

        final LocalDate today = LocalDate.now();
        final LocalDate endDate = today.minusMonths(6);
        for (LocalDate date = today; date.isAfter(endDate); date = date.minusWeeks(1)) {
            LocalDate begin = date.with(previousOrSame(MONDAY));
            LocalDate end = date.with(nextOrSame(SUNDAY));

            int divisor = 0;
            double sum = 0.0;
            for (SingleRslResult result : singleRslResults) {
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

            RslResult result = new RslResult(begin, end.isAfter(today) ? today : end, average);
            log.info("Complete RSL calculated {}", result);
            cumulateRslResult.add(result);
        }

        return cumulateRslResult;
    }
}
