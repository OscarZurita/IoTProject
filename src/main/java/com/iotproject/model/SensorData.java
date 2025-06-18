package com.iotproject.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
public class SensorData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "device_id", nullable = false)
    private String deviceId;
    
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "device_timestamp")
    private LocalDateTime deviceTimestamp;

    @NotNull
    @Column(name = "moisture", nullable = false)
    private Integer moisture;

    @NotNull
    @Column(name = "light", nullable = false)
    private Double light;

    @NotNull
    @Column(name = "decision", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean decision;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }

    // Default constructor
    public SensorData() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public LocalDateTime getDeviceTimestamp(){
        return deviceTimestamp;
    }
    public void setDeviceTimestamp(LocalDateTime deviceTimestamp){
        this.deviceTimestamp = deviceTimestamp;
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

    public Boolean getDecision(){
        return decision;
    }
    public void setDecision(Boolean decision){
        this.decision = decision;
    }
} 