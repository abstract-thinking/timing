package com.example.timing.control.gi.domain;

public record DailyUSDollarEuroInterestRate(Double value, Status status) {

    public boolean isMissing() {
        return status == Status.MISSING;
    }
}
