package com.example.timing.services.quotes;

import com.example.timing.control.rsl.Indices;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static java.util.Calendar.YEAR;

@Slf4j
@SpringBootTest
public class QuotesServiceIT {

    @Autowired
    private QuotesService quotesService;

    @Test
    public void shouldFetchDax() {
        Calendar to = new GregorianCalendar(2020, Calendar.MARCH, 2);
        Calendar from = (Calendar) to.clone();
        from.add(YEAR, -1);
        String[] symbols = {Indices.DAX.getSymbol()};
        List<HistoryQuote> historyQuotes = quotesService.fetchQuotes(symbols, from, to);

        log.info(String.valueOf(historyQuotes));
    }
}
