package com.example.timing;

import lombok.Builder;
import lombok.Value;

import java.time.YearMonth;

@Builder
@Value
public class GiPartResult {

        YearMonth date;
        double rate;

        YearMonth comparativeDate;
        double comparativeRate;

        int point;
}
