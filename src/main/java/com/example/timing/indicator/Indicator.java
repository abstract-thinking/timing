package com.example.timing.indicator;

import java.time.YearMonth;

public interface Indicator {
    PartialIndicatorResult indicate(YearMonth yearMonth);
}
