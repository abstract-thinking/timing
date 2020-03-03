package com.example.timing.indicator;

import com.example.timing.boundary.gi.PartialIndicatorResult;

import java.time.YearMonth;

public interface Indicator {
    PartialIndicatorResult indicate(YearMonth yearMonth);
}
