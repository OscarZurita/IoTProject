package com.iotproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iotproject.model.SensorData;
import com.iotproject.repository.SensorDataRepository;

import jakarta.transaction.Transactional;

@Service
public class SensorDataService {
    
    SensorDataRepository sensorDataRepository;

    @Autowired
    public SensorDataService(SensorDataRepository sensorDataRepository) {
        this.sensorDataRepository = sensorDataRepository;
    }

    @Transactional
    public SensorData saveSensorData(SensorData sensorData) {
        SensorData res =  sensorDataRepository.save(sensorData);
        
        if(sensorData.getMoisture() < 30 && sensorData.getWateringStatus() == false) {
            res.setWateringStatus(true);
        } else if (sensorData.getMoisture() > 30 && sensorData.getWateringStatus() == true) {
            res.setWateringStatus(false);
        }

        return res;
    }

}
