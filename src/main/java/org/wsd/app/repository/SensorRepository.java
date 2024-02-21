package org.wsd.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.wsd.app.event.SensorEvent;

public interface SensorRepository extends MongoRepository<SensorEvent, Long> {
}
