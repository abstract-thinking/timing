package com.example.timing.boundary.rsl;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "rslresults")
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
