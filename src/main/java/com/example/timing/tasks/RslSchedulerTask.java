package com.example.timing.tasks;

import com.example.timing.data.RslRepository;
import com.example.timing.service.QuotesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RslSchedulerTask {

    private final QuotesService service;

    private final RslRepository repository;

    @Autowired
    public RslSchedulerTask(QuotesService service, RslRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    // Every Sunday, 1:03
    @Scheduled(cron = "0 3 1 ? * SUN")
    public void doRsl() {
        log.info("Scheduling RSL task");
        repository.saveAll(service.fetchAll());
    }
}

