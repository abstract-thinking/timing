package com.example.timing.services.quotes;

import com.example.timing.control.rsl.Indices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import static com.example.timing.control.rsl.Indices.DAX;
import static java.util.Calendar.MARCH;
import static java.util.Calendar.YEAR;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class QuotesServiceIT {

    @Autowired
    private QuotesService quotesService;

    @Test
    public void shouldFetchDax() {
        Calendar to = new GregorianCalendar(2020, MARCH, 2);
        Calendar from = (Calendar) to.clone();
        from.add(YEAR, -1);
        String[] symbols = {DAX.getSymbol()};
        Map<Indices, List<HistoryQuote>> historyQuotes = quotesService.fetchQuotes(symbols, from, to);

        assertThat(historyQuotes.values()).isNotEmpty().hasSize(1);
        assertThat(historyQuotes.get(DAX)).isNotEmpty().hasSizeGreaterThanOrEqualTo(50);
    }
}
