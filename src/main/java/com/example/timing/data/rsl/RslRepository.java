package com.example.timing.data.rsl;

import com.example.timing.boundary.rsl.RslResult;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RslRepository extends MongoRepository<RslResult, String> {
}
