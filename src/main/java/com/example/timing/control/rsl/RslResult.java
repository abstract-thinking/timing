package com.example.timing.control.rsl;

import lombok.ToString;
import lombok.Value;

import java.time.LocalDate;

@ToString
@Value
public class RslResult {
    Indices symbol;
    LocalDate date;
    Double rsl;
}
