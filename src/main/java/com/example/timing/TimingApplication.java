package com.example.timing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TimingApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimingApplication.class, args);
    }

}
