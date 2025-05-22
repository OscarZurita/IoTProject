package com.iotproject.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import java.util.Map;

@Service
public class WeatherService {
    
   private static final String API_KEY = "5893176a2f4144a3840140049252205"; 

    public Map<String, Object> getWeather(String city) {
        String url = UriComponentsBuilder
                .fromUriString("http://api.weatherapi.com/v1/current.json")
                .queryParam("key", API_KEY)
                .queryParam("q", city)
                .build().toString();

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        return response;
    }
} 