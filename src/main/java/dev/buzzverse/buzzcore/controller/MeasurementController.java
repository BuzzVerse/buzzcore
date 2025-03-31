package dev.buzzverse.buzzcore.controller;

import dev.buzzverse.buzzcore.model.BME280Measurement;
import dev.buzzverse.buzzcore.model.BatteryMeasurement;
import dev.buzzverse.buzzcore.model.MeasurementParameters;
import dev.buzzverse.buzzcore.service.MeasurementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/measurements")
@RequiredArgsConstructor
public class MeasurementController {

    private final MeasurementService measurementService;

    @GetMapping("/battery")
    public List<BatteryMeasurement> getBatteryMeasurements(@ModelAttribute MeasurementParameters params) {
        params.applyDefaults();
        return measurementService.getBatteryMeasurements(params);
    }

    @GetMapping("/bme280")
    public List<BME280Measurement> getBme280Measurements(@ModelAttribute MeasurementParameters params) {
        params.applyDefaults();
        return measurementService.getBme280Measurements(params);
    }

}
