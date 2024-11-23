package com.example.eventlogging.service;

import com.example.eventlogging.model.EventLog;
import com.example.eventlogging.repository.EventLogRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.List;

@Service
public class EventLogService {

    private final EventLogRepository repository;

    public EventLogService(EventLogRepository repository) {
        this.repository = repository;
    }

    public EventLog saveEvent(EventLog eventLog, String previousHash) {
        eventLog.setTimestamp(Instant.now());
        eventLog.setPreviousHash(previousHash);
        eventLog.setHash(generateHash(eventLog));
        return repository.save(eventLog);
    }

    public List<EventLog> getAllEvents() {
        return repository.findAll();
    }

    private String generateHash(EventLog eventLog) {
        String dataToHash = eventLog.getEventType() +
                            eventLog.getTimestamp().toString() +
                            eventLog.getSourceAppId() +
                            eventLog.getPayload() +
                            eventLog.getPreviousHash();
        return sha256Hex(dataToHash);
    }

    private String sha256Hex(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error while generating hash: " + e.getMessage(), e);
        }
    }
}
