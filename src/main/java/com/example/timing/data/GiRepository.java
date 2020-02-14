package com.example.timing.data;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GiRepository extends MongoRepository<IndicatorResult, String> {
    List<IndicatorResult> findBySumOfPointsIsNotOrderByDateDesc(int sumOfPoints);
}
