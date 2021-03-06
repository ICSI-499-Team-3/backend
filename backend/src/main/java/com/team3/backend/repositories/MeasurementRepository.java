package com.team3.backend.repositories;

import com.team3.backend.models.Measurement;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Tony Comanzo
 */
public interface MeasurementRepository extends MongoRepository<Measurement, String> {
}
