package dev.buzzverse.buzzcore.service;

import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import dev.buzzverse.buzzcore.client.InfluxDBClientManager;
import dev.buzzverse.buzzcore.model.BME280Measurement;
import dev.buzzverse.buzzcore.model.BatteryMeasurement;
import dev.buzzverse.buzzcore.utils.InfluxUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeasurementService {

    private final InfluxDBClientManager influxDBClientManager;

    public List<BatteryMeasurement> getBatteryMeasurements(
            Instant startTime,
            Instant endTime,
            String deviceEui,
            boolean latest
    ) {
        String fluxQuery = InfluxUtils.buildFluxQuery(
                "battery",
                startTime,
                endTime,
                deviceEui,
                latest
        );

        List<FluxTable> tables = influxDBClientManager.query(fluxQuery);
        List<BatteryMeasurement> result = new ArrayList<>();

        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                BatteryMeasurement battery = new BatteryMeasurement();
                battery.setTime(record.getTime());
                battery.setDeviceEui((String) record.getValueByKey("deviceEui"));
                battery.setIsExternalPower(InfluxUtils.safeCastBoolean(record.getValueByKey("isExternalPower")));
                battery.setBatteryLevel(InfluxUtils.safeCastFloat(record.getValueByKey("batteryLevel")));

                result.add(battery);
            }
        }
        return result;
    }

    public List<BME280Measurement> getBme280Measurements(
            Instant startTime,
            Instant endTime,
            String deviceEui,
            boolean latest
    ) {
        String fluxQuery = InfluxUtils.buildFluxQuery(
                "bme280",
                startTime,
                endTime,
                deviceEui,
                latest
        );

        List<FluxTable> tables = influxDBClientManager.query(fluxQuery);
        List<BME280Measurement> result = new ArrayList<>();

        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                BME280Measurement bme = new BME280Measurement();
                bme.setTime(record.getTime());
                bme.setDeviceEui((String) record.getValueByKey("deviceEui"));
                bme.setTemperature(InfluxUtils.safeCastInt(record.getValueByKey("temperature")));
                bme.setHumidity(InfluxUtils.safeCastInt(record.getValueByKey("humidity")));
                bme.setPressure(InfluxUtils.safeCastInt(record.getValueByKey("pressure")));
                bme.setRssi(InfluxUtils.safeCastInt(record.getValueByKey("rssi")));
                bme.setSnr(InfluxUtils.safeCastFloat(record.getValueByKey("snr")));

                result.add(bme);
            }
        }
        return result;
    }

}
