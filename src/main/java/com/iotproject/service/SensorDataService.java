package com.iotproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.iotproject.model.SensorData;
import com.iotproject.repository.SensorDataRepository;

import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

@Service
public class SensorDataService {
    
    private final SensorDataRepository sensorDataRepository;
    private final WeatherService weatherService;

    // Moisture thresholds
    private static final int MOISTURE_WET = 1300;
    private static final int MOISTURE_DRY = 1700;
    
    // Light threshold
    private static final int LIGHT_CLOUDY = 1700;

    @Autowired
    public SensorDataService(SensorDataRepository sensorDataRepository, WeatherService weatherService) {
        this.sensorDataRepository = sensorDataRepository;
        this.weatherService = weatherService;
    }

    @Transactional
    public ResponseEntity<?> saveSensorData(SensorData sensorData) {
        // Save the sensor data
        SensorData savedData = sensorDataRepository.save(sensorData);
        
        // Create response map
        Map<String, Object> response = new HashMap<>();
        response.put("sensorData", savedData);
        
        // Check if watering is needed
        boolean needsWatering = checkWateringNeeds(sensorData);
        response.put("needsWatering", needsWatering);
        
        return ResponseEntity.ok(response);
    }

    private boolean checkWateringNeeds(SensorData sensorData) {
        // If moisture is already wet, no need to water
        if (sensorData.getMoisture() <= MOISTURE_WET) {
            return false;
        }
        
        // If moisture is dry, check weather conditions
        else {
            // Get weather data
            Map<String, Object> weatherData = weatherService.getForecast("Emden");
            
            // Check forecast for rain
            Map<String, Object> forecast = (Map<String, Object>) weatherData.get("forecast");
            List<Map<String, Object>> forecastDays = (List<Map<String, Object>>) forecast.get("forecastday");
            Map<String, Object> todayForecast = forecastDays.get(0);
            Map<String, Object> day = (Map<String, Object>) todayForecast.get("day");
            
            // Don't water if it's currently raining
            Map<String, Object> current = (Map<String, Object>) weatherData.get("current");
            double precipMm = ((Number) current.get("precip_mm")).doubleValue();
            if (precipMm > 0) {
                return false;
            }
            
            int chanceOfRain = ((Number) day.get("daily_chance_of_rain")).intValue();
            // Don't water if there's a high chance of rain
            if (chanceOfRain > 60) {
                return false;
            }
            
            // If 
            if (sensorData.getLight() < LIGHT_CLOUDY && sensorData.getMoisture() <= MOISTURE_DRY) {
                return false; // Don't water if it's cloudy
            }
            
            return true; // Need to water
        }
        
    }

    public Page<SensorData> getAllSensorData(Pageable pageable) {
        return sensorDataRepository.findAll(pageable);
    }

    public Optional<SensorData> getSensorDataById(Long id) {
        return sensorDataRepository.findById(id);
    }

    public Page<SensorData> getLatestDataForAllDevices(Pageable pageable) {
        return sensorDataRepository.findLatestDataForAllDevices(pageable);
    }

    public Long countDistinctDevices() {
        return sensorDataRepository.countDistinctDevices();
    }

    public SensorData getLatestDataByDeviceId(Long deviceId) {
        return sensorDataRepository.findLatestDataByDeviceId(deviceId);
    }

    public Page<SensorData> getAllDataByDeviceId(Long deviceId, Pageable pageable) {
        return sensorDataRepository.findByDeviceId(deviceId, pageable);
    }
}
