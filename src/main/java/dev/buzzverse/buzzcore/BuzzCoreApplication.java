package dev.buzzverse.buzzcore;

import dev.buzzverse.buzzcore.BuzzVerse_V1_BME280.BME280Data;
import dev.buzzverse.buzzcore.BuzzVerse_V1_Packet.Packet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BuzzCoreApplication {

    public static void main(String[] args) {
        BME280Data bme280 = BME280Data.newBuilder()
                .setTemperature(10)
                .setPressure(21)
                .setHumidity(37)
                .build();

        Packet packet = Packet.newBuilder()
                .setBme280(bme280)
                .build();

        System.out.println("Temperature: " + packet.getBme280().getTemperature());
        System.out.println("Pressure:    " + packet.getBme280().getPressure());
        System.out.println("Humidity:    " + packet.getBme280().getHumidity());

        SpringApplication.run(BuzzCoreApplication.class, args);
    }

}
