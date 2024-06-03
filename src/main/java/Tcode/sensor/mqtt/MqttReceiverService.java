package Tcode.sensor.mqtt;

import Tcode.sensor.model.po.SensorData;
import Tcode.sensor.service.SensorDataService;
import jakarta.annotation.PostConstruct;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MqttReceiverService {

    @Value("${mqtt.broker.url}")
    private String brokerUrl;

    @Value("${mqtt.client.id}")
    private String clientId;

    @Value("${mqtt.topic}")
    private String topic;

    @Autowired
    private SensorDataService sensorDataService;

    private MqttClient client;
    private ObjectMapper objectMapper = new ObjectMapper();

//    @PostConstruct
//    public void start() throws MqttException {
//        client = new MqttClient(brokerUrl, clientId);
//        client.connect();
//        client.subscribe(topic, this::messageArrived);
//    }

    private void messageArrived(String topic, MqttMessage message) {
        String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
        try {
            SensorData sensorData = objectMapper.readValue(payload, SensorData.class);
            sensorDataService.saveSensorData(sensorData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
