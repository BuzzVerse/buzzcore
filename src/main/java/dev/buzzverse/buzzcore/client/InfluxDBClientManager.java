package dev.buzzverse.buzzcore.client;

import com.influxdb.LogLevel;
import com.influxdb.client.*;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.query.FluxTable;
import dev.buzzverse.buzzcore.config.InfluxDbProperties;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InfluxDBClientManager {

    private final InfluxDBClient influxDBClient;
    private final WriteApiBlocking writeApi;
    private final QueryApi queryApi;

    public InfluxDBClientManager(InfluxDbProperties properties) {
        InfluxDBClientOptions options = InfluxDBClientOptions.builder()
                .url(properties.getUrl())
                .authenticate(properties.getUsername(), properties.getPassword().toCharArray())
                .org(properties.getOrg())
                .bucket(properties.getBucket())
                .logLevel(LogLevel.BASIC)
                .build();

        this.influxDBClient = InfluxDBClientFactory.create(options);
        this.writeApi = influxDBClient.getWriteApiBlocking();
        this.queryApi = influxDBClient.getQueryApi();
    }

    public <T> void writeMeasurement(T measurement) {
        writeApi.writeMeasurement(WritePrecision.NS, measurement);
    }

    public List<FluxTable> query(String fluxQuery) {
        return queryApi.query(fluxQuery);
    }

    @PreDestroy
    public void close() {
        influxDBClient.close();
    }
}