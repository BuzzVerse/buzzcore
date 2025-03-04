package dev.buzzverse.buzzcore;

import dev.buzzverse.buzzcore.config.GrpcServerProperties;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.security.BearerTokenAuthenticationInterceptor;
import ttn.lorawan.v3.AppAsGrpc;

@Configuration
@RequiredArgsConstructor
public class GrpcClientConfig {

    private final GrpcServerProperties grpcServerProperties;

    @Bean
    public ManagedChannel managedChannel() {
        return ManagedChannelBuilder.forAddress(grpcServerProperties.getHost(), grpcServerProperties.getPort())
                .useTransportSecurity()
                .intercept(new BearerTokenAuthenticationInterceptor(grpcServerProperties.getApiKey()))
                .build();
    }

    @Bean
    public AppAsGrpc.AppAsStub asyncStub(ManagedChannel channel) {
        return AppAsGrpc.newStub(channel);
    }

    @Bean
    public AppAsGrpc.AppAsBlockingStub blockingStub(ManagedChannel channel) {
        return AppAsGrpc.newBlockingStub(channel);
    }
}