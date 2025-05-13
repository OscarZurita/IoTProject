package com.iotproject.repository;

import com.iotproject.model.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Long> {
    // Basic CRUD operations are automatically implemented by Spring Data JPA
} 