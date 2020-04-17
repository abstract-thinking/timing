package com.example.timing.services.time;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TimeService {

    public LocalDate getToDate() {
        return LocalDate.now();
    }
}
