package com.example.timing.control.gi.domain;

import java.time.YearMonth;

public record HarmonisedIndexOfConsumerPrices(YearMonth period, Double value, Status status) {

    public boolean isEstimated() {
        return status == Status.ESTIMATED;
    }
}
