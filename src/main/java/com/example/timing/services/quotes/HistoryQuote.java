package com.example.timing.services.quotes;

import com.example.timing.control.rsl.Indices;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
public class HistoryQuote {
    Indices symbol;
    LocalDate date;
    BigDecimal adjClose;
}
