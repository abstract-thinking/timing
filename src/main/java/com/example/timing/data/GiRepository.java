package com.example.timing.data;

import org.springframework.data.repository.CrudRepository;

import java.time.YearMonth;
import java.util.List;

public interface GiRepository extends CrudRepository<GiResult, String> {
    List<GiResult> findBySumOfPointsIsNot(int sumOfPoints);
}
