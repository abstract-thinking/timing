package com.example.timing.control.rsl;

import lombok.ToString;
import lombok.Value;

import java.time.LocalDate;

@ToString
@Value
public class SingleRslResult {
    Indices symbol;
    LocalDate date;
    Double rsl;
}
