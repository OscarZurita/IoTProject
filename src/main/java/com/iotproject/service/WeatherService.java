package com.iotproject.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeatherService {
    
    private static final String API_KEY = "5893176a2f4144a3840140049252205"; 

    public Map<String, Object> getForecast(String city) {
        String url = UriComponentsBuilder
                .fromUriString("http://api.weatherapi.com/v1/forecast.json")
                .queryParam("key", API_KEY)
                .queryParam("q", city)
                .queryParam("days", 1)
                .build().toString();

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        return response;
    }

    public Map<String, Object> getCurrentWeather(String city){
        String url = UriComponentsBuilder
                .fromUriString("http://api.weatherapi.com/v1/current.json")
                .queryParam("key", API_KEY)
                .queryParam("q", city)
                .queryParam("days", 1)
                .build().toString();

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        return response;
    }

    public Map<String, Object> getFilteredCurrentWeather(String city) {
        Map<String, Object> fullResponse = getCurrentWeather(city);
        Map<String, Object> current = (Map<String, Object>) fullResponse.get("current");
        Map<String, Object> condition = (Map<String, Object>) current.get("condition");
        
        Map<String, Object> filteredResponse = new HashMap<>();
        filteredResponse.put("temperature", current.get("temp_c"));
        filteredResponse.put("is_raining", condition.get("text").toString().toLowerCase().contains("rain"));
        
        return filteredResponse;
    }
} 