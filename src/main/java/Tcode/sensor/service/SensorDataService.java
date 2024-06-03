package Tcode.sensor.service;


import Tcode.sensor.mapper.SensorDataMapper;
import Tcode.sensor.model.Enum.SensorType;
import Tcode.sensor.model.po.SensorData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SensorDataService {

    @Autowired
    private SensorDataMapper sensorDataMapper;

    public List<SensorData> getAllSensorData() {
        return sensorDataMapper.findAll();
    }

    public List<SensorData> getSensorDataByType(SensorType type) {
        return sensorDataMapper.findByType(type);
    }


    public List<SensorData> getSensorDataByTypeAndTimestampRange(SensorType type, LocalDateTime startTime, LocalDateTime endTime) {
        return sensorDataMapper.findByTypeAndTimestampRange(type,startTime,endTime);
    }

    public List<SensorData> getAllSensorDataByTimestampRange(LocalDateTime startTime, LocalDateTime endTime) {
        return sensorDataMapper.findAllByTimestampBetween(startTime, endTime);
    }

    public void saveSensorData(SensorData sensorData) {
        sensorData.setTimestamp(LocalDateTime.now());
        sensorDataMapper.save(sensorData);
    }
}
