package com.justice.spotify_service.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransferServiceClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${services.transfer-service-url:http://localhost:8083}")
    private String transferServiceUrl;

    public Mono<Object> forwardExecuteTransfer(String userId, Object requestBody) {
        return webClientBuilder.build()
                .post()
                .uri(transferServiceUrl + "/api/transfer/execute")
                .header("X-User-Id", userId)
                .bodyValue(requestBody != null ? requestBody : Map.of())
                .retrieve()
                .bodyToMono(Object.class)
                .doOnError(e -> log.error("Failed to forward execute transfer to transfer-service: {}", e.getMessage()));
    }
}
