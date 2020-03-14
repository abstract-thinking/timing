package com.example.timing.services.quotes;

import com.example.timing.control.rsl.Indices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.example.timing.control.rsl.Indices.DAX;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class QuotesServiceIT {

    @Autowired
    private QuotesService quotesService;

    @Test
    public void shouldFetchDax() {
        LocalDate toDate = LocalDate.now();
        LocalDate fromDate = toDate.minusYears(1);

        String[] symbols = {DAX.getSymbol()};
        Map<Indices, List<HistoryQuote>> historyQuotes = quotesService.fetchQuotes(symbols, fromDate, toDate);

        assertThat(historyQuotes.values()).isNotEmpty().hasSize(1);
        assertThat(historyQuotes.get(DAX)).isNotEmpty().hasSizeGreaterThanOrEqualTo(50);
    }
}
