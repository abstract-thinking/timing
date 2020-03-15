package com.example.timing.services.quotes;

import com.example.timing.control.rsl.Indices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.example.timing.control.rsl.Indices.DAX;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class QuotesServiceIT {

    @Autowired
    private QuotesService quotesService;

    @Test
    public void shouldFetchDax() {
        LocalDate toDate = LocalDate.now();
        LocalDate fromDate = toDate.minusYears(1);

        List<String> symbol = singletonList(DAX.getSymbol());
        Map<Indices, List<HistoryQuote>> historyQuotes = quotesService.fetchQuotes(symbol, fromDate, toDate);

        assertThat(historyQuotes).isNotEmpty().hasSize(1).containsKey(DAX);
        assertThat(historyQuotes.get(DAX)).isNotEmpty().hasSizeGreaterThanOrEqualTo(50);
    }
}
