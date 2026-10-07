package com.justice.transfer_service.client;

import com.justice.transfer_service.dto.SpotifyTrackDto;
import com.justice.transfer_service.dto.TrackAnalysisResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SpotifyServiceClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${services.spotify-service-url:http://localhost:8082}")
    private String spotifyServiceUrl;

    public Mono<List<SpotifyTrackDto>> getPlaylistTracks(String userId, String playlistId) {
        return webClientBuilder.build()
                .get()
                .uri(spotifyServiceUrl + "/api/spotify/playlists/" + playlistId + "/tracks")
                .header("X-User-Id", userId)
                .retrieve()
                .bodyToFlux(SpotifyTrackDto.class)
                .collectList()
                .doOnError(e -> {
                    if (e instanceof org.springframework.web.reactive.function.client.WebClientResponseException wre) {
                        log.error("Failed to call spotify-service for playlist {}. Status: {}, Body: {}", playlistId, wre.getStatusCode(), wre.getResponseBodyAsString());
                    } else {
                        log.error("Failed to call spotify-service for playlist {}: {}", playlistId, e.getMessage());
                    }
                })
                .onErrorResume(org.springframework.web.reactive.function.client.WebClientResponseException.Forbidden.class, ex -> {
                    log.warn("Access forbidden (403) from spotify-service for playlist {}. User {}. Returning empty tracks list. Details: {}", playlistId, userId, ex.getResponseBodyAsString());
                    return Mono.just(List.of());
                })
                .onErrorResume(org.springframework.web.reactive.function.client.WebClientResponseException.Unauthorized.class, ex -> {
                    log.error("Unauthorized (401) from spotify-service for user {}", userId);
                    return Mono.error(new org.springframework.web.server.ResponseStatusException(
                            org.springframework.http.HttpStatus.UNAUTHORIZED,
                            "Spotify authorization expired for user " + userId + ". Please log in with Spotify again."));
                });
    }

    public Mono<TrackAnalysisResultDto> analyzeTrack(String userId, SpotifyTrackDto track) {
        java.util.Map<String, String> body = java.util.Map.of(
                "sourceTrackId", track.getTrackId() != null ? track.getTrackId() : "",
                "title", track.getTitle() != null ? track.getTitle() : "",
                "artist", track.getArtists() != null ? track.getArtists() : "",
                "primaryArtist", track.getPrimaryArtist() != null ? track.getPrimaryArtist() : ""
        );

        return webClientBuilder.build()
                .post()
                .uri(spotifyServiceUrl + "/api/spotify/analyze-track")
                .header("X-User-Id", userId)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(com.justice.transfer_service.dto.TrackAnalysisResultDto.class)
                .doOnError(e -> log.error("Failed to call spotify-service analyze-track for {}: {}", track.getTitle(), e.getMessage()));
    }

    public Mono<com.justice.transfer_service.dto.CreatePlaylistResponseDto> createPlaylist(String userId, String name, String description) {
        java.util.Map<String, String> body = java.util.Map.of(
                "name", name != null ? name : "Transferred Playlist",
                "description", description != null ? description : "Transferred via PlaySwap"
        );

        return webClientBuilder.build()
                .post()
                .uri(spotifyServiceUrl + "/api/spotify/playlists/create")
                .header("X-User-Id", userId)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(com.justice.transfer_service.dto.CreatePlaylistResponseDto.class)
                .doOnError(e -> log.error("Failed to call spotify-service create-playlist for {}: {}", name, e.getMessage()));
    }

    public Mono<Boolean> addTracksToPlaylist(String userId, String playlistId, List<String> trackIds) {
        java.util.Map<String, Object> body = java.util.Map.of(
                "trackIds", trackIds != null ? trackIds : List.of()
        );

        return webClientBuilder.build()
                .post()
                .uri(spotifyServiceUrl + "/api/spotify/playlists/" + playlistId + "/add-tracks")
                .header("X-User-Id", userId)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Boolean.class)
                .doOnError(e -> log.error("Failed to call spotify-service add-tracks for playlist {}: {}", playlistId, e.getMessage()))
                .onErrorReturn(false);
    }
}
