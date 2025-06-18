package com.iotproject.controller;

import com.iotproject.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public Map<String, Object> getWeather() {
        try {
            String city = Files.readString(Paths.get("config/city.txt")).trim();
            return weatherService.getFilteredCurrentWeather(city);
        } catch (Exception e) {
            throw new RuntimeException("Could not read city from config/city.txt: " + e.getMessage());
        }
    }

    @PutMapping("/city")
    public ResponseEntity<?> changeCity(@RequestBody String newCity) {
        String cityTrimmed = newCity.trim();
        if (cityTrimmed == null || cityTrimmed.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "City must not be empty"));
        }
        Map<String, Object> weatherResponse;
        try {
            weatherResponse = weatherService.getCurrentWeather(cityTrimmed);
            if (weatherResponse.containsKey("error")) {
                return ResponseEntity.badRequest().body(Map.of("error", "City not found in weather service"));
            }
        } catch (HttpClientErrorException ex) {
            String responseBody = ex.getResponseBodyAsString();
            if (responseBody != null && responseBody.contains("No matching location found")) {
                return ResponseEntity.badRequest().body(Map.of("error", "City not found in weather service"));
            }
            return ResponseEntity.status(500).body(Map.of("error", "Weather service error: " + ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(Map.of("error", "Weather service error: " + ex.getMessage()));
        }
        try {
            Files.writeString(Paths.get("config/city.txt"), cityTrimmed);
            return ResponseEntity.ok(Map.of("message", "City updated successfully", "city", cityTrimmed));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to update city: " + e.getMessage()));
        }
    }
}