package Tcode.sensor.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SensorSimulator {

    private static final String BROKER_URL = "tcp://8.138.133.153:1883";
    private static final String CLIENT_ID = "sensorSimulator";
    private static final String TOPIC = "sensor/data";

    private MqttClient client;
    private Random random = new Random();

    public SensorSimulator() throws MqttException {
        client = new MqttClient(BROKER_URL, CLIENT_ID);
        client.connect();
    }

    public void start() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    sendSensorData();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 5000); // 每5秒发送一次数据
    }

    private void sendSensorData() throws MqttException {
        double temperature = 20 + random.nextDouble() * 10; // 模拟温度在20-30之间
        double humidity = 30 + random.nextDouble() * 40;    // 模拟湿度在30-70之间
        double lightIntensity = 300 + random.nextDouble() * 500; // 模拟光强在300-800之间

        String payload = String.format("{\"type\":\"TEMPERATURE\",\"value\":%.2f}", temperature);
        client.publish(TOPIC, new MqttMessage(payload.getBytes()));

        payload = String.format("{\"type\":\"HUMIDITY\",\"value\":%.2f}", humidity);
        client.publish(TOPIC, new MqttMessage(payload.getBytes()));

        payload = String.format("{\"type\":\"LIGHT_INTENSITY\",\"value\":%.2f}", lightIntensity);
        client.publish(TOPIC, new MqttMessage(payload.getBytes()));
    }

    public static void main(String[] args) {
        try {
            SensorSimulator simulator = new SensorSimulator();
            simulator.start();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
