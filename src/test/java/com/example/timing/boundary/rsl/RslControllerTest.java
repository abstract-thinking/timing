package com.example.timing.boundary.rsl;

import com.example.timing.control.rsl.RslCalculator;
import com.example.timing.services.quotes.QuotesService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RslControllerTest {

    @MockBean
    private RslCalculator calculator;

    @MockBean
    private QuotesService service;

    @Test
    public void should() throws Exception {
        when(service.fetchQuotes(any(), any(), any())).thenReturn(emptyList());

    }
}
