package com.iotproject.controller;

import com.iotproject.model.SensorData;
import com.iotproject.repository.SensorDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensor-data")
public class SensorDataController {

    private final SensorDataRepository sensorDataRepository;

    @Autowired
    public SensorDataController(SensorDataRepository sensorDataRepository) {
        this.sensorDataRepository = sensorDataRepository;
    }

    @PostMapping
    public ResponseEntity<SensorData> createSensorData(@RequestBody SensorData sensorData) {
        return ResponseEntity.ok(sensorDataRepository.save(sensorData));
    }

    @GetMapping
    public ResponseEntity<List<SensorData>> getAllSensorData() {
        return ResponseEntity.ok(sensorDataRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SensorData> getSensorDataById(@PathVariable Long id) {
        return sensorDataRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/latest")
    public ResponseEntity<SensorData> getLatestSensorData() {
        return sensorDataRepository.findAll().stream()
                .reduce((first, second) -> second)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
} 