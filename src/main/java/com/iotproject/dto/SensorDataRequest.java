package com.iotproject.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

public class SensorDataRequest {
    
    @NotNull(message = "Device ID is required")
    private String deviceId;

    @NotNull(message = "Moisture value is required")
    @Min(value = 0, message = "Moisture must be greater than or equal to 0")
    private Integer moisture;

    @NotNull(message = "Light value is required")
    @Min(value = 0, message = "Light must be greater than or equal to 0")
    private Double light;

    @PastOrPresent(message = "Timestamp cannot be in the future")
    private LocalDateTime deviceTimestamp;

    // Getters and Setters
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getMoisture() {
        return moisture;
    }

    public void setMoisture(Integer moisture) {
        this.moisture = moisture;
    }

    public Double getLight() {
        return light;
    }

    public void setLight(Double light) {
        this.light = light;
    }

    public LocalDateTime getDeviceTimestamp(){
        return deviceTimestamp;
    }
    public void setDeviceTimestamp(LocalDateTime deviceTimestamp){
        this.deviceTimestamp = deviceTimestamp;
    }
} 