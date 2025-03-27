package dev.buzzverse.buzzcore.controller;

import dev.buzzverse.buzzcore.model.BME280Measurement;
import dev.buzzverse.buzzcore.model.BatteryMeasurement;
import dev.buzzverse.buzzcore.service.MeasurementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/measurements")
@RequiredArgsConstructor
public class MeasurementController {

    private final MeasurementService measurementService;

    @GetMapping("/battery")
    public List<BatteryMeasurement> getBatteryMeasurements(
            @RequestParam(required = false) String deviceEui,
            @RequestParam(required = false, defaultValue = "1970-01-01T00:00:00Z") Instant start,
            @RequestParam(required = false) Instant end,
            @RequestParam(required = false, defaultValue = "false") boolean latest
    ) {
        Instant endTime = (end != null) ? end : Instant.now();
        return measurementService.getBatteryMeasurements(start, endTime, deviceEui, latest);
    }

    @GetMapping("/bme280")
    public List<BME280Measurement> getBme280Measurements(
            @RequestParam(required = false) String deviceEui,
            @RequestParam(required = false, defaultValue = "1970-01-01T00:00:00Z") Instant start,
            @RequestParam(required = false) Instant end,
            @RequestParam(required = false, defaultValue = "false") boolean latest
    ) {
        Instant endTime = (end != null) ? end : Instant.now();
        return measurementService.getBme280Measurements(start, endTime, deviceEui, latest);
    }
}
