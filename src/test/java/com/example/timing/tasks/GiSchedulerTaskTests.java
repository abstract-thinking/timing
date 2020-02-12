package com.example.timing.tasks;

import com.example.timing.data.GiRepository;
import com.example.timing.data.IndicatorResult;
import com.example.timing.web.RatesService;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    public void shouldSaveIndicator() {
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

        when(ratesService.processExchangeRates()).thenReturn(rates);
        when(ratesService.processInflationRates()).thenReturn(rates);
        when(ratesService.processInterestRates()).thenReturn(interestRates);
        when(repository.save(any(IndicatorResult.class))).thenAnswer((Answer<IndicatorResult>) invocation -> {
            Object[] args = invocation.getArguments();
            IndicatorResult result = (IndicatorResult) args[0];
            result.setId("abcdefgh");
            return result;
        });

        IndicatorResult result = task.processGi();

        verify(repository).save(any(IndicatorResult.class));

        assertThat(result.getId()).isEqualTo("abcdefgh");
    }
}
