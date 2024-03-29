package com.example.timing.boundary.gi.api;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.time.YearMonth;

@Builder
@Data
public class IndicatorResult {

    private LocalDate date;

    private int seasonPoint;

    private double interestRate;
    private int interestPoint;

    private double inflationRate;
    private double inflationRateOneYearAgo;
    private int inflationPoint;

    private double exchangeRate;
    private double exchangeRageOneYearAgo;
    private int exchangePoint;

    private int sumOfPoints;

    @Getter(AccessLevel.NONE)
    private boolean shouldInvest;

    public boolean shouldInvest() {
        return shouldInvest;
    }
}
