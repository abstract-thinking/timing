package com.example.timing.control.rsl;

import lombok.Value;

import java.time.LocalDate;

@Value
public class SingleRslResult {
    Indices symbol;
    LocalDate date;
    Double rsl;
}
