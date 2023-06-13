package com.example.timing.control.gi;

import java.time.LocalDate;

public interface Indicator {
    PartialIndicatorResult indicate(LocalDate yearMonth);
}
