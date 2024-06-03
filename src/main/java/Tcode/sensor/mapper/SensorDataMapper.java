package Tcode.sensor.mapper;


import Tcode.sensor.model.Enum.SensorType;
import Tcode.sensor.model.po.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SensorDataMapper extends JpaRepository<SensorData,Long> {

    List<SensorData> findByType(SensorType type);

    //List<SensorData> findByTypeAndTimestampRange(SensorType type, Timestamp startTime, Timestamp endTime);
    /**
     * JPA虽然可以省略代码,但要尽可能保证其方法名是对应的，否则启动就会出错
     * */
    @Query("SELECT s FROM SensorData s WHERE s.type = :type AND s.timestamp BETWEEN :startTime AND :endTime")
    List<SensorData> findByTypeAndTimestampRange(@Param("type") SensorType type,
                                                 @Param("startTime") LocalDateTime startTime,
                                                 @Param("endTime") LocalDateTime endTime);

    List<SensorData> findAllByTimestampBetween(LocalDateTime startTime, LocalDateTime endTime);
}
