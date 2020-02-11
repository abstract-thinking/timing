package com.example.timing.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.YearMonth;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class GiRepositoryTests {

    @Autowired
    GiRepository repository;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    public void shouldSetIdOnSave() {
        IndicatorResult indicatorResult = repository.save(IndicatorResult.builder().build());

        assertThat(indicatorResult.getId()).isNotNull();
    }

    @Test
    public void shouldFindBySumOfPointsIsNot() {
        IndicatorResult result1 = IndicatorResult.builder()
                .date(YearMonth.now())
                .sumOfPoints(2)
                .build();

        IndicatorResult result2 = IndicatorResult.builder()
                .date(YearMonth.now().minusMonths(1))
                .sumOfPoints(3)
                .build();

        repository.save(result1);
        repository.save(result2);

        List<IndicatorResult> results = repository.findBySumOfPointsIsNot(2);

        assertThat(results).containsExactly(result2);
    }
}
