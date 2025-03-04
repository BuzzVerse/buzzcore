package dev.buzzverse.buzzcore.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "grpc.server")
public class GrpcServerProperties {
    private String applicationId;
    private String apiKey;
    private String host;
    private int port;
}