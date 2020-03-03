package com.example.timing.data.gi;

import com.example.timing.boundary.gi.IndicatorResult;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.YearMonth;
import java.util.List;

public interface GiRepository extends MongoRepository<IndicatorResult, String> {
    IndicatorResult findOneByDate(YearMonth date);

    List<IndicatorResult> findBySumOfPointsIsNotOrderByDateDesc(int sumOfPoints);
}
