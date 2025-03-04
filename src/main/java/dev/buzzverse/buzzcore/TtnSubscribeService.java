package dev.buzzverse.buzzcore;

import com.google.protobuf.ByteString;
import dev.buzzverse.buzzcore.BuzzVerse_V1_BME280.BME280Data;
import dev.buzzverse.buzzcore.BuzzVerse_V1_Packet.Packet;
import dev.buzzverse.buzzcore.config.GrpcServerProperties;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ttn.lorawan.v3.AppAsGrpc;
import ttn.lorawan.v3.Uplink;

@Service
@AllArgsConstructor
public class TtnSubscribeService {

    private final AppAsGrpc.AppAsStub subscribeStub;
    private final GrpcServerProperties grpcServerProperties;

    public void startSubscription() {
        Uplink.ApplicationIdentifiers request = Uplink.ApplicationIdentifiers.newBuilder()
                .setApplicationId(grpcServerProperties.getApplicationId())
                .build();

        subscribeStub.subscribe(request, new StreamObserver<>() {
            @Override
            public void onNext(Uplink.ApplicationUp value) {
                decodeUplink(value);
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Subscription error: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Subscription completed");
            }
        });
    }

    private void decodeUplink(Uplink.ApplicationUp uplink) {
        Uplink.ApplicationUplink uplinkMessage = uplink.getUplinkMessage();
        ByteString payload = uplinkMessage.getFrmPayload();

        try {
            Packet packet = Packet.parseFrom(payload);
            if (packet.hasBme280()) {
                BME280Data bme280Data = packet.getBme280();

                int temperature = bme280Data.getTemperature();
                int pressure = bme280Data.getPressure() + 1000;
                int humidity = bme280Data.getHumidity();

                System.out.println("Temperature: " + temperature + "°C");
                System.out.println("Humidity: " + humidity + "%");
                System.out.println("Pressure: " + pressure + " hPa");
            }

        } catch (Exception e) {
            System.err.println("Error decoding payload: " + e.getMessage());
        }
    }

}