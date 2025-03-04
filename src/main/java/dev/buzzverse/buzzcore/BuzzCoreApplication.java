package dev.buzzverse.buzzcore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.grpc.autoconfigure.client.GrpcClientAutoConfiguration;

@SpringBootApplication
@ImportAutoConfiguration(GrpcClientAutoConfiguration.class)
public class BuzzCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(BuzzCoreApplication.class, args);
    }

}
