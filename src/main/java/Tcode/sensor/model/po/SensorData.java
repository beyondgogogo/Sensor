package Tcode.sensor.model.po;


import Tcode.sensor.model.Enum.SensorType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "数据信息实体")
@Entity
@Data
public class SensorData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //数据类型
    @Enumerated(EnumType.STRING)
    private SensorType type;

    //数据值
    private float value;

    //记录时间
    private LocalDateTime timestamp;
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public SensorType getType() {
        return type;
    }

    public void setType(SensorType type) {
        this.type = type;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
