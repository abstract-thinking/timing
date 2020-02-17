package com.example.timing.tasks;

import com.example.timing.data.GiRepository;
import com.example.timing.results.IndicatorResult;
import com.example.timing.service.RatesService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// TODO: Fixme
@Disabled("Test too much and broken")
@SpringBootTest
public class GiSchedulerTaskTests {

    @MockBean
    private RatesService ratesService;

    @MockBean
    private GiRepository repository;

    @Autowired
    private GiSchedulerTask task;

    // TODO: Should I mock the date?
    // Missing: repository.findBySumOfPointsIsNot(2).get(0).shouldInvest()
    @Test
    public void shouldSaveIndicator() throws IOException {
        LocalDate now = LocalDate.now();

        Map<YearMonth, Double> rates = new HashMap<>();
        rates.put(YearMonth.from(now), 1.0);
        rates.put(YearMonth.from(now.minusMonths(1)), 1.5);
        rates.put(YearMonth.from(now.minusMonths(2)), 1.4);
        rates.put(YearMonth.from(now.minusMonths(12)), 0.7);
        rates.put(YearMonth.from(now.minusMonths(13)), 1.0);
        rates.put(YearMonth.from(now.minusMonths(14)), 1.3);

        Map<LocalDate, Double> interestRates = new HashMap<>();
        interestRates.put(LocalDate.of(2020, Month.DECEMBER, 10), 1.0);
        interestRates.put(LocalDate.of(2015, Month.MARCH, 10), 0.1);

        // Because of Application - can I avoid this an isolate
        when(ratesService.readExchangeRates()).thenReturn(rates);
        when(ratesService.readInflationRates()).thenReturn(rates);
        when(ratesService.readInterestRates()).thenReturn(interestRates);
        when(ratesService.fetchExchangeRates()).thenReturn(rates);
        when(ratesService.fetchInflationRates()).thenReturn(rates);
        when(ratesService.fetchInterestRates()).thenReturn(interestRates);

        task.doGi();

        // Awesome
        verify(repository, times(590)).save(any(IndicatorResult.class));
    }
}
