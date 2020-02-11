package com.example.timing.research;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@EnableScheduling
@ComponentScan("com.example.timing")
public class ScheduledConfig {

    @Bean
    AtomicInteger count() {
        return new AtomicInteger(0);
    }
}

