package dev.buzzverse.buzzcore.model;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Measurement(name = BME280Measurement.MEASUREMENT_KEY)
@SuperBuilder
@Data
public class BME280Measurement extends TimeSeriesMeasurement {

    public static final String MEASUREMENT_KEY = "bme280";

    @Column
    private Integer temperature;

    @Column
    private Integer humidity;

    @Column
    private Integer pressure;

}
