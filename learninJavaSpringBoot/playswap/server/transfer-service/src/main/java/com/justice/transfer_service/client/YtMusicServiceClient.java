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
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class YtMusicServiceClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${services.ytmusic-service-url:http://localhost:8081}")
    private String ytmusicServiceUrl;

    public Mono<List<SpotifyTrackDto>> getPlaylistTracks(String userId, String playlistId) {
        return webClientBuilder.build()
                .get()
                .uri(ytmusicServiceUrl + "/api/ytmusic/playlists/" + playlistId + "/tracks")
                .header("X-User-Id", userId)
                .retrieve()
                .bodyToFlux(SpotifyTrackDto.class)
                .collectList()
                .doOnError(e -> log.error("Failed to fetch YouTube playlist tracks for playlist {}: {}", playlistId, e.getMessage()))
                .onErrorReturn(List.of());
    }

    public Mono<TrackAnalysisResultDto> analyzeTrack(String userId, SpotifyTrackDto track) {
        Map<String, String> body = Map.of(
                "spotifyTrackId", track.getTrackId() != null ? track.getTrackId() : "",
                "title", track.getTitle() != null ? track.getTitle() : "",
                "artist", track.getArtists() != null ? track.getArtists() : "",
                "primaryArtist", track.getPrimaryArtist() != null ? track.getPrimaryArtist() : ""
        );

        return webClientBuilder.build()
                .post()
                .uri(ytmusicServiceUrl + "/api/ytmusic/analyze-track")
                .header("X-User-Id", userId)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(TrackAnalysisResultDto.class)
                .doOnError(e -> log.error("Failed to call ytmusic-service analyze-track for {}: {}", track.getTitle(), e.getMessage()));
    }

    public Mono<com.justice.transfer_service.dto.CreatePlaylistResponseDto> createPlaylist(String userId, String name, String description) {
        Map<String, String> body = Map.of(
                "name", name != null ? name : "Transferred Playlist",
                "description", description != null ? description : "Transferred via PlaySwap"
        );

        return webClientBuilder.build()
                .post()
                .uri(ytmusicServiceUrl + "/api/ytmusic/playlists/create")
                .header("X-User-Id", userId)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(com.justice.transfer_service.dto.CreatePlaylistResponseDto.class)
                .doOnError(e -> log.error("Failed to call ytmusic-service create-playlist for {}: {}", name, e.getMessage()));
    }

    public Mono<Boolean> addTracksToPlaylist(String userId, String playlistId, List<String> videoIds) {
        Map<String, Object> body = Map.of(
                "videoIds", videoIds != null ? videoIds : List.of()
        );

        return webClientBuilder.build()
                .post()
                .uri(ytmusicServiceUrl + "/api/ytmusic/playlists/" + playlistId + "/add-tracks")
                .header("X-User-Id", userId)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Boolean.class)
                .doOnError(e -> log.error("Failed to call ytmusic-service add-tracks for playlist {}: {}", playlistId, e.getMessage()))
                .onErrorReturn(false);
    }
}
