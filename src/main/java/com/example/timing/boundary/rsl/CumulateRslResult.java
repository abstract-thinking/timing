package com.example.timing.boundary.rsl;

import lombok.ToString;
import lombok.Value;

import java.time.LocalDate;

@ToString
@Value
public class CumulateRslResult {
    LocalDate begin;
    LocalDate end;
    Double rsl;
}
