package dev.buzzverse.buzzcore.model;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Measurement(name = BatteryMeasurement.MEASUREMENT_KEY)
@SuperBuilder
@Data
public class BatteryMeasurement extends TimeSeriesMeasurement {

    public static final String MEASUREMENT_KEY = "battery";

    @Column
    private Boolean isExternalPower;

    @Column
    private Float batteryLevel;

}
