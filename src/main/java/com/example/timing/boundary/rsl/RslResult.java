package com.example.timing.boundary.rsl;

import lombok.Value;

import java.time.LocalDate;

@Value
public class RslResult {
    LocalDate begin;
    LocalDate end;
    Double rsl;
}
