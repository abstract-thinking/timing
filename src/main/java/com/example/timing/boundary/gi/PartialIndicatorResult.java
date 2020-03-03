package com.example.timing.boundary.gi;


import lombok.Builder;
import lombok.Value;

import java.time.YearMonth;

@Builder
@Value
public class PartialIndicatorResult {

    YearMonth date;
    double rate;

    YearMonth comparativeDate;
    double comparativeRate;

    int point;
}
