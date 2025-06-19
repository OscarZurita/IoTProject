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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

@Service
public class SensorDataService {
    
    private final SensorDataRepository sensorDataRepository;
    private final WeatherService weatherService;
    private final MailService mailService;

    // Moisture thresholds
    private static final int MOISTURE_WET = 1300; //under this is very wet
    private static final int MOISTURE_DRY = 1700; //over this is very dry
    
    // Light threshold
    private static final int LIGHT_CLOUDY = 1700; //over this is bright

    @Autowired
    public SensorDataService(SensorDataRepository sensorDataRepository, WeatherService weatherService, MailService mailService) {
        this.sensorDataRepository = sensorDataRepository;
        this.weatherService = weatherService;
        this.mailService = mailService;
    }

    @Transactional
    public ResponseEntity<?> saveSensorData(SensorData sensorData) {
        // Handle exceptions internally
        boolean needsWatering = checkWateringNeeds(sensorData);
        sensorData.setDecision(needsWatering);
        
        Map<String, Object> response = new HashMap<>();
        SensorData savedData = sensorDataRepository.save(sensorData);
        response.put("sensorData", savedData);
        return ResponseEntity.ok(response);
    }

    private boolean checkWateringNeeds(SensorData sensorData) {
        // If moisture is already wet, no need to water
        if (sensorData.getMoisture() <= MOISTURE_WET) {
            return false;
        }

        else if(sensorData.getMoisture() >= MOISTURE_DRY){
            if(sensorData.getMoisture() >= 2000){
                System.out.println("SOIL DRY");
                String message = "Soil too dry detected on device: "+ sensorData.getDeviceId();
                String emailRecipient = "";
                try {
                    emailRecipient = Files.readString(Paths.get("config/email.txt")).trim();
                } catch (IOException e) {
                    System.err.println("Failed to read email recipient from config/email.txt: " + e.getMessage());
                    // Optionally, set a fallback or skip sending
                    return true;
                }
                mailService.sendEmail(emailRecipient, "DRY WARNING", message);
            }
            return true; //If moisture is very dry water directly
        }
        
        // If moisture is somewhat dry, check weather conditions
        else {
            try {
                String city = Files.readString(Paths.get("config/city.txt")).trim();
                Map<String, Object> weatherData = weatherService.getForecast(city);
                
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
                if (sensorData.getLight() < LIGHT_CLOUDY) {
                    return false; // Don't water if it's cloudy
                }
                
                return true;
            } catch (Exception e) {
                return true; // Water if we don't know weather
            }
        }
    }

    public Page<SensorData> getAllSensorData(Pageable pageable) {
        return sensorDataRepository.findAll(pageable);
    }

    public Optional<SensorData> getSensorDataById(Long id) {
        return sensorDataRepository.findById(id);
    }

    public List<SensorData> getLatestDataForAllDevices() {
        return sensorDataRepository.findLatestDataForAllDevices();
    }

    public Long countDistinctDevices() {
        return sensorDataRepository.countDistinctDevices();
    }

    public SensorData getLatestDataByDeviceId(String deviceId) {
        return sensorDataRepository.findLatestDataByDeviceId(deviceId);
    }

    public Page<SensorData> getAllDataByDeviceId(String deviceId, Pageable pageable) {
        return sensorDataRepository.findByDeviceId(deviceId, pageable);
    }
}
