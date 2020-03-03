package com.example.timing.services.quotes;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SingleRslResult {

    private LocalDate date;

    private String symbol;

    private Double rsl;

    public SingleRslResult(LocalDate date, String symbol, Double rsl) {
        this.date = date;
        this.symbol = symbol;
        this.rsl = rsl;
    }
}
