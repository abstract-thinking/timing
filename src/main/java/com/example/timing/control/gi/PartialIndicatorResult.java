package com.example.timing.control.gi;

import lombok.Builder;
import lombok.Value;

import java.time.YearMonth;

@Builder
@Value
class PartialIndicatorResult {

    YearMonth date;
    double rate;

    YearMonth comparativeDate;
    double comparativeRate;

    int point;
}
