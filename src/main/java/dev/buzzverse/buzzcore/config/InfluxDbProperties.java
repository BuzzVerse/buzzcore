package dev.buzzverse.buzzcore.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "influx")
public class InfluxDbProperties {
    private String url;
    private String username;
    private String password;
    private String org;
    private String bucket;
}
