package com.team3.backend.repositories;

import com.team3.backend.models.Log;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface LogRepository extends MongoRepository<Log, String> {

    @Query("{email:'?0'}")
    List<Log> findByEmail(String email);
}