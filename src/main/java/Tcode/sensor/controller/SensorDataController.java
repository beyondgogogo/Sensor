package Tcode.sensor.controller;


import Tcode.sensor.model.Enum.SensorType;
import Tcode.sensor.model.po.SensorData;
import Tcode.sensor.service.SensorDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
@Tag(name = "数据接收控制器")
@RestController
@RequestMapping("/api/sensor-data")
public class SensorDataController {

    @Autowired
    private SensorDataService sensorDataService;

    @Operation(summary = "获取所有类型数据信息")
    @GetMapping
    public List<SensorData> getAllSensorData() {
        return sensorDataService.getAllSensorData();
    }

    @Operation(summary = "按类型获取数据信息")
    @GetMapping("/type")
    public List<SensorData> getSensorDataByType(@RequestParam(value = "type",required = false) SensorType type) {
        if(type==null){
            return sensorDataService.getAllSensorData();
        }
        return sensorDataService.getSensorDataByType(type);
    }


    @Operation(summary = "按类型和时间范围获取数据信息")
    @GetMapping("/type/range")
    public List<SensorData> getSensorDataByTypeAndTimestampRange(
            @RequestParam(value = "type", required = false) SensorType type,
            @RequestParam(value = "startTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(value = "endTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        if (type == null && startTime == null && endTime == null) {
            // 如果没有任何参数，返回所有数据
            return sensorDataService.getAllSensorData();
        } else if (type == null) {
            // 如果没有类型参数，返回时间范围内的所有数据
            return sensorDataService.getAllSensorDataByTimestampRange(startTime, endTime);
        } else {
            // 返回特定类型和时间范围的数据
            return sensorDataService.getSensorDataByTypeAndTimestampRange(type, startTime, endTime);
        }
    }

}
