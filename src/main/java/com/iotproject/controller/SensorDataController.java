package com.iotproject.controller;

import com.iotproject.model.SensorData;
import com.iotproject.dto.SensorDataRequest;
import com.iotproject.service.SensorDataService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/sensor-data")
public class SensorDataController {

    private final SensorDataService sensorDataService;

    @Autowired
    public SensorDataController(SensorDataService sensorDataService) {
        this.sensorDataService = sensorDataService;
    }

    @PostMapping
    public ResponseEntity<?> createSensorData(@Valid @RequestBody SensorDataRequest request) {
        SensorData sensorData = new SensorData();
        sensorData.setDeviceId(request.getDeviceId());
        sensorData.setMoisture(request.getMoisture());
        sensorData.setLight(request.getLight());
        
        return ResponseEntity.ok(sensorDataService.saveSensorData(sensorData).getBody());
    }

    @GetMapping
    public ResponseEntity<Page<SensorData>> getAllSensorData(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? 
            Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        
        return ResponseEntity.ok(sensorDataService.getAllSensorData(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SensorData> getSensorDataById(@PathVariable Long id) {
        return sensorDataService.getSensorDataById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/latest")
    public ResponseEntity<List<SensorData>> getLatestDataForAllDevices() {
        return ResponseEntity.ok(sensorDataService.getLatestDataForAllDevices());
    }

    @GetMapping("/device/{deviceId}/latest")
    public ResponseEntity<SensorData> getLatestDataByDeviceId(@PathVariable String deviceId) {
        SensorData latestData = sensorDataService.getLatestDataByDeviceId(deviceId);
        if (latestData == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(latestData);
    }

    @GetMapping("/device/{deviceId}")
    public ResponseEntity<Page<SensorData>> getAllDataByDeviceId(
            @PathVariable String deviceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(sensorDataService.getAllDataByDeviceId(deviceId, pageable));
    }
} 