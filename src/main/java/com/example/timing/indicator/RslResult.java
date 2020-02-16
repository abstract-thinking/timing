package com.example.timing.indicator;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Data
public class RslResult {

    @Id
    private String id;

    private LocalDate date;

    private String symbol;

    private Double rsl;

    public RslResult(LocalDate date, String symbol, Double rsl) {
        this.date = date;
        this.symbol = symbol;
        this.rsl = rsl;
    }
}
