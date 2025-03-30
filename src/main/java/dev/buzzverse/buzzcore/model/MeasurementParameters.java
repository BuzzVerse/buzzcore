package dev.buzzverse.buzzcore.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Data
@NoArgsConstructor
public class MeasurementParameters {
    private Instant startTime;
    private Instant endTime;
    private String deviceEui;
    private boolean latest;

    /**
     * Applies default values for startTime and endTime if they are not provided.
     * Defaults: startTime to last 24 hours, endTime to now.
     */
    public void applyDefaults() {
        Instant now = Instant.now();
        if (startTime == null) {
            startTime = now.minus(1, ChronoUnit.DAYS);
        }
        if (endTime == null) {
            endTime = now;
        }
    }
}