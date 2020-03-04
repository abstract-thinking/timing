package com.example.timing.control.rsl;

import com.example.timing.boundary.rsl.RslResult;
import com.example.timing.services.quotes.HistoryQuote;
import com.example.timing.services.quotes.QuotesService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.previousOrSame;
import static java.util.Calendar.YEAR;
import static java.util.Locale.GERMAN;
import static java.util.stream.Collectors.groupingBy;

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
        return doCalculation(allSymbols());
    }

    private List<RslResult> doCalculation(String[] symbols) {
        Calendar today = Calendar.getInstance();
        Calendar from = (Calendar) today.clone();
        from.add(YEAR, ONE_YEAR_BEFORE);

        List<HistoryQuote> historicalQuotes = service.fetchQuotes(symbols, from, today);

        List<SingleRslResult> singleRslResults = new ArrayList<>();
        for (int i = 0; i < historicalQuotes.size() - PAST_WEEKS; ++i) {
            HistoryQuote historyQuote = historicalQuotes.get(i);
            Double rsl = historyQuote.getAdjClose().doubleValue() /
                    calculateAverage(historicalQuotes.subList(i, historicalQuotes.size()));
            singleRslResults.add(new SingleRslResult(historyQuote.getSymbol(), historyQuote.getDate(), rsl));
        }

        Map<LocalDate, List<SingleRslResult>> byWeek = singleRslResults.stream()
                .collect(groupingBy(d -> d.getDate().with(previousOrSame(SUNDAY))));

        final DayOfWeek firstDayOfWeek = WeekFields.of(GERMAN).getFirstDayOfWeek();
        final DayOfWeek lastDayOfWeek = DayOfWeek.of(((firstDayOfWeek.getValue() + 5) % DayOfWeek.values().length) + 1);

        List<RslResult> results = new ArrayList<>(byWeek.size());
        for (Map.Entry<LocalDate, List<SingleRslResult>> entry : byWeek.entrySet()) {
            LocalDate date = entry.getKey();

            results.add(new RslResult(
                    date.with(TemporalAdjusters.previousOrSame(firstDayOfWeek)), // first day
                    date.with(TemporalAdjusters.nextOrSame(lastDayOfWeek)),      // last day
                    entry.getValue().stream().mapToDouble(SingleRslResult::getRsl).average().getAsDouble()));
        }

        results.sort(Comparator.comparing(RslResult::getBegin).reversed());

        return results;
    }

    private double calculateAverage(List<HistoryQuote> historicalQuotes) {
        return historicalQuotes.stream()
                .filter(hq -> hq.getAdjClose() != null)
                .limit(PAST_WEEKS)
                .mapToDouble(hq -> hq.getAdjClose().doubleValue())
                .average()
                .orElse(Double.NaN);
    }

    private String[] allSymbols() {
        return EnumSet.allOf(Indices.class).stream().map(Indices::getSymbol).toArray(String[]::new);
    }

}
