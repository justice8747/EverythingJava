package com.justice.ytmusic_service.client;

import com.justice.ytmusic_service.model.YoutubePlaylist;
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
public class YoutubeApiClient {

    private final WebClient.Builder webClientBuilder;

    private static final String YOUTUBE_API_BASE =
            "https://www.googleapis.com/youtube/v3";

    public Mono<List<YoutubePlaylist>> getUserPlaylists(String accessToken) {
        return webClientBuilder.build()
                .get()
                .uri(YOUTUBE_API_BASE +
                        "/playlists?part=snippet,contentDetails&mine=true&maxResults=50")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    List<Map<String, Object>> items =
                            (List<Map<String, Object>>) response.get("items");

                    return items.stream().map(item -> {
                        YoutubePlaylist playlist = new YoutubePlaylist();

                        // ID
                        playlist.setId((String) item.get("id"));

                        // Snippet contains name, description, thumbnail
                        Map<String, Object> snippet =
                                (Map<String, Object>) item.get("snippet");
                        playlist.setName((String) snippet.get("title"));
                        playlist.setDescription((String) snippet.get("description"));

                        // Thumbnail
                        Map<String, Object> thumbnails =
                                (Map<String, Object>) snippet.get("thumbnails");
                        if (thumbnails != null) {
                            Map<String, Object> defaultThumb =
                                    (Map<String, Object>) thumbnails.get("default");
                            if (defaultThumb != null) {
                                playlist.setImageUrl(
                                        (String) defaultThumb.get("url"));
                            }
                        }

                        // Total tracks
                        Map<String, Object> contentDetails =
                                (Map<String, Object>) item.get("contentDetails");
                        if (contentDetails != null) {
                            playlist.setTotalTracks(
                                    (Integer) contentDetails.get("itemCount"));
                        }

                        return playlist;
                    }).collect(Collectors.toList());
                })
                .doOnError(e -> log.error(
                        "Failed to fetch YouTube playlists: {}", e.getMessage()));
    }
}