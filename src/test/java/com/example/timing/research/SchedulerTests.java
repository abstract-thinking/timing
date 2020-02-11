package com.example.timing.research;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
// @SpringJUnitConfig(ScheduledConfig.class)
public class SchedulerTests {

    @Autowired
    Counter counter;

    @Test
    public void testCronScheduler() {
        counter.scheduled();

        assertThat(counter.getInvocationCount()).isEqualTo(1);
    }
}
