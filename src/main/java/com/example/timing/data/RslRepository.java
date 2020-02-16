package com.example.timing.data;

import com.example.timing.indicator.RslResult;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RslRepository extends MongoRepository<RslResult, String> {
}
