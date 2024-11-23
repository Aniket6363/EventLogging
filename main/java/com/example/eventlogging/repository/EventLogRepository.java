package com.example.eventlogging.repository;

import com.example.eventlogging.model.EventLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventLogRepository extends MongoRepository<EventLog, String> {
    List<EventLog> findByEventType(String eventType);
    List<EventLog> findBySourceAppId(String sourceAppId);
}
