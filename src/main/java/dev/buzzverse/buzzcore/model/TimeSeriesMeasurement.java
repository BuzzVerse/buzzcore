package dev.buzzverse.buzzcore.model;

import com.influxdb.annotations.Column;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class TimeSeriesMeasurement {

    @Column(timestamp = true)
    @Builder.Default
    protected Instant time = Instant.now();

    @Column(tag = true)
    protected String deviceEui;

    @Column
    protected Integer rssi;

    @Column
    protected Float snr;

}