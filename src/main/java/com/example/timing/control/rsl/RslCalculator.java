package com.example.timing.control.rsl;

import com.example.timing.boundary.rsl.RslResult;
import com.example.timing.services.quotes.QuotesService;
import com.example.timing.services.quotes.SingleRslResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.previousOrSame;
import static java.util.stream.Collectors.averagingDouble;
import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Service
public class RslCalculator {

    private final QuotesService service;

    public RslCalculator(QuotesService service) {
        this.service = service;
    }

    // TODO: Write unit tests
    public List<RslResult> calculate() {
        List<SingleRslResult> singleRslResults = service.fetchAll();

        Map<LocalDate, Double> byWeek = singleRslResults.stream()
                .collect(groupingBy(d -> d.getDate().with(previousOrSame(SUNDAY)),
                        averagingDouble(SingleRslResult::getRsl)));

        log.info(String.valueOf(byWeek));

        TemporalField fieldISO = WeekFields.of(Locale.GERMANY).dayOfWeek();
        List<RslResult> results = new ArrayList<>(byWeek.size());
        for (Map.Entry<LocalDate, Double> entry : byWeek.entrySet()) {
            results.add(new RslResult(
                    entry.getKey().with(fieldISO, 1),
                    entry.getKey().with(fieldISO, 7),
                    entry.getValue()));
        }

        results.sort(Comparator.comparing(RslResult::getBegin).reversed());

        return results;
    }

}
