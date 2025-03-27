package dev.buzzverse.buzzcore.model;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Measurement(name = "battery")
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BatteryMeasurement extends TimeSeriesMeasurement {

    @Column
    private Boolean isExternalPower;

    @Column
    private Float batteryLevel;

}
