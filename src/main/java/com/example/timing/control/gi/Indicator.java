package com.example.timing.control.gi;

import java.time.YearMonth;

public interface Indicator {
    PartialIndicatorResult indicate(YearMonth yearMonth);
}
