package com.example.timing.indicator;

import com.example.timing.results.PartialIndicatorResult;

import java.time.YearMonth;

public interface Indicator {
    PartialIndicatorResult indicate(YearMonth yearMonth);
}
