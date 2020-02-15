package com.example.timing.indicator;

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
