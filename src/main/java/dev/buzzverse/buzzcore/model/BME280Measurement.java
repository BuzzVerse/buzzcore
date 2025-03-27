package dev.buzzverse.buzzcore.model;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Measurement(name = "bme280")
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BME280Measurement extends TimeSeriesMeasurement {

    @Column
    private Integer temperature;

    @Column
    private Integer humidity;

    @Column
    private Integer pressure;

}
