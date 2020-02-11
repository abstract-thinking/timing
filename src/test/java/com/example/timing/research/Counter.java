package com.example.timing.research;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class Counter {

    private final AtomicInteger count;

    @Autowired
    public Counter(AtomicInteger count) {
        this.count = count;
    }

    @Scheduled(cron = "0 15 10 15 * ?")
    public void scheduled() {
        this.count.incrementAndGet();
    }

    public int getInvocationCount() {
        return this.count.get();
    }
}
