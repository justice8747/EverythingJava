package com.justice.auth_service.controller;

import com.justice.auth_service.entity.AccessToken;
import com.justice.auth_service.service.UserService;
import com.justice.auth_service.service.TokenRefreshService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/internal/tokens")
public class InternalTokenController {

    private final UserService userService;
    private final TokenRefreshService tokenRefreshService;
    private final String expectedInternalSecret;

    public InternalTokenController(
            UserService userService,
            TokenRefreshService tokenRefreshService,
            @Value("${app.internal-api-secret}") String expectedInternalSecret) {
        this.userService = userService;
        this.tokenRefreshService = tokenRefreshService;
        this.expectedInternalSecret = expectedInternalSecret;
    }

    @GetMapping("/{userId}/{platform}")
    public Mono<ResponseEntity<Map<String, String>>> getToken(
            @PathVariable String userId,
            @PathVariable String platform,
            @RequestHeader(value = "X-Internal-Secret", required = false) String internalSecret) {

        // Validate internal secret
        if (internalSecret == null || expectedInternalSecret == null || expectedInternalSecret.isEmpty() || !internalSecret.equals(expectedInternalSecret)) {
            log.warn("Unauthorized or mismatched internal API secret attempt for user {}. Received: '{}', Expected: '{}'", userId, internalSecret, expectedInternalSecret);
            return Mono.just(ResponseEntity.status(403).build());
        }

        log.info("Fetching token for userId: {} and platform: {}", userId, platform);

        UUID userUuid;
        try {
            userUuid = UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid UUID format for userId: {}", userId);
            return Mono.just(ResponseEntity.status(404).build());
        }

        return userService.getTokens(userUuid, platform)
                .flatMap(tokenRecord -> {
                    // Check if token is expired, missing expiresAt, or expiring in next 5 minutes
                    boolean needsRefresh = tokenRecord.getExpiresAt() == null ||
                            tokenRecord.getExpiresAt().isBefore(Instant.now().plusSeconds(300));

                    if (needsRefresh) {
                        log.info("Token for user {} on platform {} is expired/expiring. Attempting refresh...", userUuid, platform);
                        return tokenRefreshService
                                .refresh(tokenRecord, platform)
                                .map(newToken -> ResponseEntity.ok(Map.of(
                                        "accessToken", newToken.getAccessToken(),
                                        "platform", platform
                                )))
                                .onErrorResume(e -> {
                                    log.error("Failed to refresh {} token for user {}: {}. Falling back to existing access token.",
                                            platform, userUuid, e.getMessage());
                                    if (tokenRecord.getAccessToken() != null && !tokenRecord.getAccessToken().isBlank()) {
                                        return Mono.just(ResponseEntity.ok(Map.of(
                                                "accessToken", tokenRecord.getAccessToken(),
                                                "platform", platform
                                        )));
                                    }
                                    return Mono.just(ResponseEntity.status(401).<Map<String, String>>build());
                                });
                    }

                    return Mono.just(ResponseEntity.ok(Map.of(
                            "accessToken", tokenRecord.getAccessToken(),
                            "platform", platform
                    )));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("No token record found for user {} on platform {}", userUuid, platform);
                    return Mono.just(ResponseEntity.status(404).build());
                }))
                .onErrorResume(e -> {
                    log.error("Unexpected error in InternalTokenController for user {}: {}", userUuid, e.getMessage(), e);
                    return Mono.just(ResponseEntity.status(500).build());
                });
    }
}