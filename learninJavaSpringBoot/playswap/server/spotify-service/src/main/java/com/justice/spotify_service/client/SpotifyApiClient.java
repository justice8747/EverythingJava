package com.justice.spotify_service.client;

import com.justice.spotify_service.model.SpotifyPlaylist;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class SpotifyApiClient {

    private final WebClient.Builder webClientBuilder;

    private static final String SPOTIFY_API_BASE = "https://api.spotify.com/v1";

    public Mono<List<SpotifyPlaylist>> getUserPlaylists(String accessToken) {
        return webClientBuilder.build()
                .get()
                .uri(SPOTIFY_API_BASE + "/me/playlists?limit=50")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    List<Map<String, Object>> items =
                            (List<Map<String, Object>>) response.get("items");

                    return items.stream().map(item -> {
                        SpotifyPlaylist playlist = new SpotifyPlaylist();
                        playlist.setId((String) item.get("id"));
                        playlist.setName((String) item.get("name"));
                        playlist.setDescription((String) item.get("description"));

                        // Total tracks
                        Map<String, Object> tracks =
                                (Map<String, Object>) item.get("tracks");
                        playlist.setTotalTracks((Integer) tracks.get("total"));

                        // Image
                        List<Map<String, Object>> images =
                                (List<Map<String, Object>>) item.get("images");
                        if (images != null && !images.isEmpty()) {
                            playlist.setImageUrl((String) images.get(0).get("url"));
                        }

                        return playlist;
                    }).collect(Collectors.toList());
                })
                .doOnError(e -> log.error(
                        "Failed to fetch Spotify playlists: {}", e.getMessage()));
    }
}
