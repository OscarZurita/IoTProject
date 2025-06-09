package com.iotproject.repository;

import com.iotproject.model.SensorData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Long> {
    // Basic CRUD operations are automatically implemented by Spring Data JPA

    // Find latest sensor data for each device with pagination
    @Query("SELECT s FROM SensorData s WHERE s.timestamp = (SELECT MAX(s2.timestamp) FROM SensorData s2 WHERE s2.deviceId = s.deviceId)")
    List<SensorData> findLatestDataForAllDevices();

    // Find latest sensor data for a specific device
    @Query("SELECT s FROM SensorData s WHERE s.deviceId = ?1 ORDER BY s.timestamp DESC LIMIT 1")
    SensorData findLatestDataByDeviceId(String deviceId);

    // Get all data for a specific device with pagination
    Page<SensorData> findByDeviceId(String deviceId, Pageable pageable);

    // Get count of unique devices
    @Query("SELECT COUNT(DISTINCT s.deviceId) FROM SensorData s")
    Long countDistinctDevices();
} 