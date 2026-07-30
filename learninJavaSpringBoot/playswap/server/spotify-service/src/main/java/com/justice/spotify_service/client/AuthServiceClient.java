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
public class AuthServiceClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${services.auth-service-url}")
    private String authServiceUrl;

    @Value("${app.internal-api-secret}")
    private String internalApiSecret;

    public Mono<String> getSpotifyAccessToken(String userId) {
        return webClientBuilder.build()
                .get()
                .uri(authServiceUrl + "/internal/tokens/" + userId + "/spotify")
                .header("X-Internal-Secret", internalApiSecret)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (String) response.get("accessToken"))
                .doOnError(e -> log.error(
                        "Failed to fetch Spotify token for user {}: {}",
                        userId, e.getMessage()));
    }
}
