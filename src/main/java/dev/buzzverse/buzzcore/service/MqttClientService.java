package dev.buzzverse.buzzcore.service;

import dev.buzzverse.buzzcore.config.MqttServerProperties;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Service;

@Service
public class MqttClientService {
    private final IMqttClient mqttClient;
    private final MqttServerProperties mqttServerProperties;

    public MqttClientService(MqttServerProperties mqttServerProperties) throws MqttException {
        this.mqttServerProperties = mqttServerProperties;
        this.mqttClient = new MqttClient(mqttServerProperties.getServerUri(), mqttServerProperties.getClientId());
        connect();
    }

    private void connect() throws MqttException {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setUserName(mqttServerProperties.getUsername());
        options.setPassword(mqttServerProperties.getPassword().toCharArray());

        mqttClient.connect(options);
        System.out.println("Connected to MQTT Broker: " + mqttServerProperties.getServerUri());
    }

    public void publish(String message, String topic) {
        try {
            if (!mqttClient.isConnected()) {
                connect();
            }
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttMessage.setQos(0);
            mqttClient.publish(topic, mqttMessage);
            System.out.println("Message published: " + message);
        } catch (MqttException e) {
            System.err.println("Error publishing MQTT message: " + e.getMessage());
        }
    }
}
