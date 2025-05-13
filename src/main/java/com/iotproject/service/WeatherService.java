package com.iotproject.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.scheduling.annotation.Scheduled;

@Service
public class WeatherService {
    
    @Value("${weather.api.key}")
    private String apiKey;
    
    @Value("${weather.api.url}")
    private String apiUrl;
    
    private final RestTemplate restTemplate;
    
    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    @Scheduled(fixedRate = 3600000) // Run every hour
    public void fetchWeatherData() {
        // TODO: Implement weather API call
        // This will be implemented based on the specific weather API you choose
    }
} 