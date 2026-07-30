package com.justice.spotify_service.controller;

import com.justice.spotify_service.client.AuthServiceClient;
import com.justice.spotify_service.client.SpotifyApiClient;
import com.justice.spotify_service.model.SpotifyPlaylist;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/spotify")
@RequiredArgsConstructor
public class SpotifyController {

    private final AuthServiceClient authServiceClient;
    private final SpotifyApiClient spotifyApiClient;

    @GetMapping("/playlists")
    public Mono<List<SpotifyPlaylist>> getPlaylists(
            @RequestHeader("X-User-Id") String userId) {

        return authServiceClient.getSpotifyAccessToken(userId)
                .flatMap(accessToken ->
                        spotifyApiClient.getUserPlaylists(accessToken));
    }
}