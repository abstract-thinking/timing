package com.example.timing.control.gi;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.time.YearMonth;

@Builder
@Value
class PartialIndicatorResult {

    LocalDate date;
    double rate;

    LocalDate comparativeDate;
    double comparativeRate;

    int point;
}
