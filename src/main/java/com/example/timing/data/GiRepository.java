package com.example.timing.data;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GiRepository extends CrudRepository<IndicatorResult, String> {
    List<IndicatorResult> findBySumOfPointsIsNot(int sumOfPoints);
}
