package com.justice.spotify_service.controller;

import com.justice.spotify_service.client.AuthServiceClient;
import com.justice.spotify_service.client.SpotifyApiClient;
import com.justice.spotify_service.model.SpotifyPlaylist;
import com.justice.spotify_service.service.PlaylistCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/spotify")
@RequiredArgsConstructor
public class SpotifyController {

    private final AuthServiceClient authServiceClient;
    private final SpotifyApiClient spotifyApiClient;
    private final PlaylistCacheService playlistCacheService;

    private final com.justice.spotify_service.service.TrackMatchingService trackMatchingService;
    private final com.justice.spotify_service.client.TransferServiceClient transferServiceClient;

    @GetMapping("/playlists")
    public Mono<List<SpotifyPlaylist>> getPlaylists(
            @RequestHeader("X-User-Id") String userId,
            @RequestParam(value = "refresh", defaultValue = "false") boolean forceRefresh) {

        if (forceRefresh) {
            log.info("Force refresh requested for user {} (Spotify playlists)", userId);
            playlistCacheService.evict(userId);
        } else {
            List<SpotifyPlaylist> cachedPlaylists = playlistCacheService.get(userId);
            if (cachedPlaylists != null) {
                return Mono.just(cachedPlaylists);
            }
        }

        return authServiceClient.getSpotifyAccessToken(userId)
                .flatMap(accessToken -> spotifyApiClient.getUserPlaylists(accessToken))
                .doOnNext(playlists -> playlistCacheService.put(userId, playlists));
    }

    @GetMapping("/playlists/{playlistId}/tracks")
    public Mono<List<com.justice.spotify_service.model.SpotifyTrack>> getPlaylistTracks(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable("playlistId") String playlistId) {
        log.info("==> [SpotifyController] Request received for playlist: '{}' | X-User-Id: '{}'", playlistId, userId);
        return authServiceClient.getSpotifyAccessToken(userId)
                .flatMap(accessToken -> spotifyApiClient.getPlaylistTracks(playlistId, accessToken));
    }

    @PostMapping("/analyze-track")
    public Mono<com.justice.spotify_service.model.TrackAnalysisResult> analyzeTrack(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody com.justice.spotify_service.model.TrackAnalysisRequest request) {
        return authServiceClient.getSpotifyAccessToken(userId)
                .flatMap(accessToken -> trackMatchingService.analyzeTrack(request, accessToken));
    }

    @PostMapping("/playlists/create")
    public Mono<com.justice.spotify_service.model.CreatePlaylistResponse> createPlaylist(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody com.justice.spotify_service.model.CreatePlaylistRequest request) {
        log.info("==> [SpotifyController] Creating Spotify playlist '{}' for user '{}'", request.getName(), userId);
        return authServiceClient.getSpotifyAccessToken(userId)
                .flatMap(accessToken -> spotifyApiClient.createPlaylist(request.getName(), request.getDescription(), accessToken));
    }

    @PostMapping("/playlists/{playlistId}/add-tracks")
    public Mono<Boolean> addTracksToPlaylist(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable("playlistId") String playlistId,
            @RequestBody com.justice.spotify_service.model.AddTracksRequest request) {
        log.info("==> [SpotifyController] Adding {} tracks to Spotify playlist '{}' for user '{}'",
                request.getTrackIds() != null ? request.getTrackIds().size() : 0, playlistId, userId);
        return authServiceClient.getSpotifyAccessToken(userId)
                .flatMap(accessToken -> spotifyApiClient.addTracksToPlaylist(playlistId, request.getTrackIds(), accessToken));
    }

    @PostMapping({"/execute/transfer", "/transfer/execute", "/execute"})
    public Mono<Object> executeTransfer(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestBody(required = false) Object requestBody) {
        log.info("==> [SpotifyController] Forwarding execute transfer request to transfer-service for user '{}'", userId);
        return transferServiceClient.forwardExecuteTransfer(userId, requestBody);
    }
}