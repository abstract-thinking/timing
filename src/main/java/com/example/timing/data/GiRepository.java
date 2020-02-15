package com.example.timing.data;

import com.example.timing.indicator.IndicatorResult;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.YearMonth;
import java.util.List;

public interface GiRepository extends MongoRepository<IndicatorResult, String> {
    IndicatorResult findOneByDate(YearMonth date);

    List<IndicatorResult> findBySumOfPointsIsNotOrderByDateDesc(int sumOfPoints);
}
