package dev.buzzverse.buzzcore.service;

import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import dev.buzzverse.buzzcore.client.InfluxDBClientManager;
import dev.buzzverse.buzzcore.model.BME280Measurement;
import dev.buzzverse.buzzcore.model.BatteryMeasurement;
import dev.buzzverse.buzzcore.model.MeasurementParameters;
import dev.buzzverse.buzzcore.utils.InfluxUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeasurementService {

    private final InfluxDBClientManager influxDBClientManager;

    public List<BatteryMeasurement> getBatteryMeasurements(MeasurementParameters params) {
        String fluxQuery = InfluxUtils.buildFluxQuery(
                BatteryMeasurement.MEASUREMENT_KEY,
                params
        );

        List<FluxTable> tables = influxDBClientManager.query(fluxQuery);
        List<BatteryMeasurement> result = new ArrayList<>();

        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                BatteryMeasurement battery = BatteryMeasurement.builder()
                        .time(record.getTime())
                        .deviceEui((String) record.getValueByKey("deviceEui"))
                        .isExternalPower(InfluxUtils.safeCastBoolean(record.getValueByKey("isExternalPower")))
                        .batteryLevel(InfluxUtils.safeCastFloat(record.getValueByKey("batteryLevel")))
                        .build();

                result.add(battery);
            }
        }
        return result;
    }

    public List<BME280Measurement> getBme280Measurements(MeasurementParameters params) {
        String fluxQuery = InfluxUtils.buildFluxQuery(
                BME280Measurement.MEASUREMENT_KEY,
                params
        );

        List<FluxTable> tables = influxDBClientManager.query(fluxQuery);
        List<BME280Measurement> result = new ArrayList<>();

        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                BME280Measurement bme = BME280Measurement.builder()
                        .time(record.getTime())
                        .deviceEui((String) record.getValueByKey("deviceEui"))
                        .temperature(InfluxUtils.safeCastInt(record.getValueByKey("temperature")))
                        .humidity(InfluxUtils.safeCastInt(record.getValueByKey("humidity")))
                        .pressure(InfluxUtils.safeCastInt(record.getValueByKey("pressure")))
                        .rssi(InfluxUtils.safeCastInt(record.getValueByKey("rssi")))
                        .snr(InfluxUtils.safeCastFloat(record.getValueByKey("snr")))
                        .build();

                result.add(bme);
            }
        }
        return result;
    }

}
