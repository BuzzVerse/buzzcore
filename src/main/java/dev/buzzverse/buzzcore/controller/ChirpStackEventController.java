package dev.buzzverse.buzzcore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import dev.buzzverse.buzzcore.BuzzVerse_V1_BME280.BME280Data;
import dev.buzzverse.buzzcore.BuzzVerse_V1_Chirpstack.UplinkEvent;
import dev.buzzverse.buzzcore.BuzzVerse_V1_Chirpstack.UplinkRxInfo;
import dev.buzzverse.buzzcore.BuzzVerse_V1_Packet.Packet;
import dev.buzzverse.buzzcore.service.MqttClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class ChirpStackEventController {

    private final MqttClientService mqttClientService;

    @PostMapping(consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void handleEvent(@RequestParam String event, @RequestBody byte[] requestBody) {
        System.out.println("Received event: " + event);

        try {
            UplinkEvent uplinkEvent = UplinkEvent.parseFrom(requestBody);
            String devEui = uplinkEvent.getDeviceInfo().getDevEui();
            ByteString payload = uplinkEvent.getData();

            Packet packet = Packet.parseFrom(payload);
            System.out.println("Parsed Packet: " + packet);

            UplinkRxInfo uplinkRxInfo = uplinkEvent.getRxInfoList().get(0);
            Integer rssi = uplinkRxInfo.getRssi();
            Float snr = uplinkRxInfo.getSnr();

            if (packet.hasBme280()) {
                BME280Data bme280Data = packet.getBme280();
                System.out.println("Received BME280 data: " + bme280Data);
                processAndPublish(bme280Data, rssi, snr, devEui);
            }

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    public void processAndPublish(BME280Data bme280Data, Integer rssi, Float snr, String devEui) {
        int temperature = bme280Data.getTemperature();
        int pressure = bme280Data.getPressure() + 1000;
        int humidity = bme280Data.getHumidity();

        System.out.println("Temperature: " + temperature + "°C");
        System.out.println("Humidity: " + humidity + "%");
        System.out.println("Pressure: " + pressure + " hPa");

        try {
            Map<String, Object> payload = Map.of(
                    "BME280", Map.of(
                            "temperature", temperature,
                            "humidity", humidity,
                            "pressure", pressure
                    ),
                    "META", Map.of(
                            "rssi", rssi,
                            "snr", snr
                    )
            );

            String jsonPayload = new ObjectMapper().writeValueAsString(payload);
            mqttClientService.publish(jsonPayload, "sensors/" + devEui + "/data");

        } catch (Exception e) {
            System.err.println("Error creating JSON payload: " + e.getMessage());
        }
    }

}
