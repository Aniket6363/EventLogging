package com.example.eventlogging.controller;

import com.example.eventlogging.model.EventLog;
import com.example.eventlogging.service.EventLogService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
@CrossOrigin("http://localhost:4200")
public class EventLogController {

    private final EventLogService service;

    public EventLogController(EventLogService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EventLog> createLog(@Valid @RequestBody EventLog eventLog) {
        String previousHash = service.getAllEvents()
                                     .stream()
                                     .reduce((first, second) -> second)
                                     .map(EventLog::getHash)
                                     .orElse(null);
        EventLog savedLog = service.saveEvent(eventLog, previousHash);
        return ResponseEntity.ok(savedLog);
    }

    @GetMapping
    public ResponseEntity<List<EventLog>> getLogs() {
        return ResponseEntity.ok(service.getAllEvents());
    }
}
