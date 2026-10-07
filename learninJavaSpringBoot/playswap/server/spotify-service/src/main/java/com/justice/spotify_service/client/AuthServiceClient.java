package com.justice.spotify_service.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
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
        log.info("==> [AuthServiceClient] Requesting token from auth-service for userId: '{}' | Secret: '{}'", userId, internalApiSecret);
        return webClientBuilder.build()
                .get()
                .uri(authServiceUrl + "/internal/tokens/" + userId + "/spotify")
                .header("X-Internal-Secret", internalApiSecret)
                .retrieve()
                .onStatus(status -> status.equals(HttpStatus.NOT_FOUND), response ->
                        Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Spotify access token not found for user: " + userId)))
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new ResponseStatusException(response.statusCode(),
                                "Client error fetching Spotify token for user: " + userId)))
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                                "Auth service error while fetching Spotify token for user: " + userId)))
                .bodyToMono(Map.class)
                .map(response -> (String) response.get("accessToken"))
                .doOnNext(token -> log.info("<== [AuthServiceClient] Successfully retrieved Spotify Access Token for userId '{}' | Token Prefix: '{}...' (Length: {})",
                        userId, token != null && token.length() > 20 ? token.substring(0, 20) : token, token != null ? token.length() : 0))
                .doOnError(e -> log.error(
                        "Failed to fetch Spotify token for user {}: {}",
                        userId, e.getMessage()));
    }
}
