package dev.buzzverse.buzzcore.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import dev.buzzverse.buzzcore.BuzzVerse_V1_BME280.BME280Data;
import dev.buzzverse.buzzcore.BuzzVerse_V1_Chirpstack.StatusEvent;
import dev.buzzverse.buzzcore.BuzzVerse_V1_Chirpstack.UplinkEvent;
import dev.buzzverse.buzzcore.BuzzVerse_V1_Chirpstack.UplinkRxInfo;
import dev.buzzverse.buzzcore.BuzzVerse_V1_Packet.Packet;
import dev.buzzverse.buzzcore.model.ChirpStackEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChirpStackEventService {

    private final MqttClientService mqttClientService;

    public void handleEvent(ChirpStackEvent event, byte[] requestBody) {
        try {
            switch (event) {
                case UP:
                    handleUplinkEvent(requestBody);
                    break;
                case STATUS:
                    handleStatusEvent(requestBody);
                    break;
                default:
                    log.error("Unknown event type: {}", event);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown event type");
            }
        } catch (Exception e) {
            log.error("Error while handling event", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while handling event");
        }
    }

    private void handleUplinkEvent(byte[] requestBody) {
        try {
            UplinkEvent uplinkEvent = UplinkEvent.parseFrom(requestBody);
            String devEui = uplinkEvent.getDeviceInfo().getDevEui();
            log.info("Received uplink event from device: {}", devEui);

            ByteString payload = uplinkEvent.getData();

            Packet packet = Packet.parseFrom(payload);

            UplinkRxInfo uplinkRxInfo = uplinkEvent.getRxInfoList().get(0);
            Integer rssi = uplinkRxInfo.getRssi();
            Float snr = uplinkRxInfo.getSnr();

            if (packet.hasBme280()) {
                BME280Data bme280Data = packet.getBme280();
                processAndPublish(bme280Data, rssi, snr, devEui);
            }

        } catch (InvalidProtocolBufferException e) {
            log.error("Error while parsing protobuf", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while parsing protobuf");
        }
    }


    private void handleStatusEvent(byte[] requestBody) {
        try {
            StatusEvent statusEvent = StatusEvent.parseFrom(requestBody);
            String devEui = statusEvent.getDeviceInfo().getDevEui();

            Boolean isExternalPower = statusEvent.getExternalPowerSource();
            Boolean isBatteryLevelUnavailable = statusEvent.getBatteryLevelUnavailable();
            Float battery = statusEvent.getBatteryLevel();

            log.info("Received status event from device: {}", devEui);
            log.info("External power source: {}", isExternalPower);
            log.info("Battery level unavailable: {}", isBatteryLevelUnavailable);
            log.info("Battery level: {}", battery);

        } catch (InvalidProtocolBufferException e) {
            log.error("Error while parsing protobuf", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while parsing protobuf");
        }
    }

    private void processAndPublish(BME280Data bme280Data, Integer rssi, Float snr, String devEui) {
        try {
            Map<String, Object> payload = buildPayload(bme280Data, rssi, snr);
            String jsonPayload = new ObjectMapper().writeValueAsString(payload);
            mqttClientService.publish(jsonPayload, "sensors/" + devEui + "/data");
        } catch (Exception e) {
            log.error("Error while processing and publishing data", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while processing and publishing data");
        }
    }

    private Map<String, Object> buildPayload(BME280Data bme280Data, Integer rssi, Float snr) {
        int temperature = bme280Data.getTemperature();
        int pressure = bme280Data.getPressure() + 1000;
        int humidity = bme280Data.getHumidity();

        log.info("Temperature: {}°C, Humidity: {}%, Pressure: {} hPa", temperature, humidity, pressure);

        return Map.of(
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
    }
}