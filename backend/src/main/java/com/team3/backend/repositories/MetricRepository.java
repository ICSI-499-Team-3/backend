package com.team3.backend.repositories;

import com.team3.backend.models.Metric;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MetricRepository extends MongoRepository<Metric, String> {

    @Query("{userId:'?0'}")
    List<Metric> findByUserId(String userId);
}
