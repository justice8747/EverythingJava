package com.justice.auth_service.service;

import com.justice.auth_service.entity.AccessToken;
import com.justice.auth_service.entity.User;
import com.justice.auth_service.repository.AccessTokenRepository;
import com.justice.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final AccessTokenRepository accessTokerepo;
    private final PlatformTransactionManager transactionManager;

    // ==========================================
    // DOOR A — Login or Register
    // ==========================================
    public Mono<User> loginOrRegister(
            String platform,
            OAuth2User oAuth2User,
            String accessToken,
            String refreshToken,
            Instant expiresAt) {

        return Mono.fromCallable(() -> {
            TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
            return transactionTemplate.execute(status -> {
                if (platform.equals("spotify")) {
                    return handleSpotifyLoginOrRegister(
                            oAuth2User, accessToken, refreshToken, expiresAt);
                } else if (platform.equals("youtube")) {
                    return handleYoutubeLoginOrRegister(
                            oAuth2User, accessToken, refreshToken, expiresAt);
                } else {
                    throw new RuntimeException("Unsupported platform: " + platform);
                }
            });
        }).subscribeOn(Schedulers.boundedElastic());
    }

    // ==========================================
    // DOOR B — Link Account
    // ==========================================
    public Mono<User> linkAccount(
            UUID userId,
            String platform,
            OAuth2User oAuth2User,
            String accessToken,
            String refreshToken,
            Instant expiresAt) {

        return Mono.fromCallable(() -> userRepo.findById(userId))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(optionalUser -> {
                    if (optionalUser.isEmpty()) {
                        return Mono.empty();
                    }
                    return Mono.fromCallable(() -> {
                        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
                        return transactionTemplate.execute(status -> {
                            User user = userRepo.findById(userId).orElseThrow();

                            if (platform.equals("spotify")) {
                                String spotifyId = oAuth2User.getAttribute("id");
                                String email = oAuth2User.getAttribute("email");

                                // Check conflict — Spotify ID already linked to another user
                                Optional<User> conflict = userRepo.findBySpotifyId(spotifyId);
                                if (conflict.isPresent() && !conflict.get().getId().equals(userId)) {
                                    throw new RuntimeException("This Spotify account is already linked to another user");
                                }

                                user.setSpotifyId(spotifyId);
                                user.setSpotifyEmail(email);
                                if (user.getEmail() == null && email != null) {
                                    user.setEmail(email);
                                }

                            } else if (platform.equals("youtube")) {
                                String youtubeId = oAuth2User.getAttribute("sub");
                                String email = oAuth2User.getAttribute("email");

                                // Check conflict — YouTube ID already linked to another user
                                Optional<User> conflict = userRepo.findByYoutubeId(youtubeId);
                                if (conflict.isPresent() && !conflict.get().getId().equals(userId)) {
                                    throw new RuntimeException("This YouTube account is already linked to another user");
                                }

                                user.setYoutubeId(youtubeId);
                                user.setYoutubeEmail(email);
                                if (user.getEmail() == null && email != null) {
                                    user.setEmail(email);
                                }
                            }

                            User savedUser = userRepo.save(user);
                            saveTokens(savedUser.getId(), platform, accessToken, refreshToken, expiresAt);
                            return savedUser;
                        });
                    }).subscribeOn(Schedulers.boundedElastic());
                });
    }

    // ==========================================
    // Internal — Get tokens for a user/platform
    // ==========================================
    public Mono<AccessToken> getTokens(UUID userId, String platform) {
        return Mono.fromCallable(() ->
                accessTokerepo.findByUserIdAndPlatform(userId, platform)
                        .orElseThrow(() -> new RuntimeException(
                                "No tokens found for user and platform"))
        ).subscribeOn(Schedulers.boundedElastic());
    }

    // ==========================================
    // Private helpers
    // ==========================================
    @Transactional
    private User handleSpotifyLoginOrRegister(
            OAuth2User oAuth2User,
            String accessToken,
            String refreshToken,
            Instant expiresAt) {

        String spotifyId = oAuth2User.getAttribute("id");
        String email = oAuth2User.getAttribute("email");

        // Find existing user or create new one
        User user = userRepo.findBySpotifyId(spotifyId)
                .map(u -> {
                    u.setSpotifyEmail(email);
                    if (u.getEmail() == null && email != null) {
                        u.setEmail(email);
                    }
                    return userRepo.save(u);
                })
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .spotifyId(spotifyId)
                            .spotifyEmail(email)
                            .email(email)
                            .build();
                    return userRepo.save(newUser);
                });

        saveTokens(user.getId(), "spotify",
                accessToken, refreshToken, expiresAt);
        return user;
    }

    @Transactional
    private User handleYoutubeLoginOrRegister(
            OAuth2User oAuth2User,
            String accessToken,
            String refreshToken,
            Instant expiresAt) {

        // Google uses "sub" as the unique user ID
        String youtubeId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");

        User user = userRepo.findByYoutubeId(youtubeId)
                .map(u -> {
                    u.setYoutubeEmail(email);
                    if (u.getEmail() == null && email != null) {
                        u.setEmail(email);
                    }
                    return userRepo.save(u);
                })
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .youtubeId(youtubeId)
                            .youtubeEmail(email)
                            .email(email)
                            .build();
                    return userRepo.save(newUser);
                });

        saveTokens(user.getId(), "youtube",
                accessToken, refreshToken, expiresAt);
        return user;
    }

    @Transactional
    private void saveTokens(
            UUID userId,
            String platform,
            String accessToken,
            String refreshToken,
            Instant expiresAt) {

        // Delete old tokens first (refresh token rotation)
        accessTokerepo.deleteByUserIdAndPlatform(userId, platform);
        accessTokerepo.flush();

        // Save new tokens
        AccessToken tokens = AccessToken.builder()
                .userId(userId)
                .platform(platform)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresAt(expiresAt)
                .build();

        accessTokerepo.save(tokens);
        log.info("Tokens saved for user {} on platform {}", userId, platform);
    }

    private User findExistingUser(String provider, String principalName) {
        if (provider.equals("spotify")) {
            return userRepo.findBySpotifyId(principalName)
                    .orElseThrow(() -> new RuntimeException("User not found for Spotify ID: " + principalName));
        } else if (provider.equals("google") || provider.equals("youtube")) {
            return userRepo.findByYoutubeId(principalName)
                    .orElseThrow(() -> new RuntimeException("User not found for YouTube ID: " + principalName));
        } else {
            throw new RuntimeException("Unsupported existing provider: " + provider);
        }
    }
}
