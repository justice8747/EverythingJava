package com.justice.ytmusic_service.controller;

import com.justice.ytmusic_service.client.AuthServiceClient;
import com.justice.ytmusic_service.client.YoutubeApiClient;
import com.justice.ytmusic_service.model.YoutubePlaylist;
import com.justice.ytmusic_service.service.PlaylistCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/ytmusic")
@RequiredArgsConstructor
public class YtmusicController {

    private final AuthServiceClient authServiceClient;
    private final YoutubeApiClient youtubeApiClient;
    private final PlaylistCacheService playlistCacheService;
    private final com.justice.ytmusic_service.service.TrackMatchingService trackMatchingService;
    private final com.justice.ytmusic_service.client.TransferServiceClient transferServiceClient;

    @GetMapping("/playlists")
    public Mono<List<YoutubePlaylist>> getPlaylists(
            @RequestHeader("X-User-Id") String userId,
            @RequestParam(value = "refresh", defaultValue = "false") boolean forceRefresh) {

        if (forceRefresh) {
            log.info("Force refresh requested for user {} (YouTube playlists)", userId);
            playlistCacheService.evict(userId);
        } else {
            List<YoutubePlaylist> cachedPlaylists = playlistCacheService.get(userId);
            if (cachedPlaylists != null) {
                return Mono.just(cachedPlaylists);
            }
        }

        return authServiceClient.getYoutubeAccessToken(userId)
                .flatMap(accessToken -> youtubeApiClient.getUserPlaylists(accessToken))
                .doOnNext(playlists -> playlistCacheService.put(userId, playlists));
    }

    @GetMapping("/playlists/{playlistId}/tracks")
    public Mono<List<com.justice.ytmusic_service.model.YoutubeTrack>> getPlaylistTracks(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable("playlistId") String playlistId) {
        log.info("==> [YtmusicController] Request received for YouTube playlist tracks: '{}' | X-User-Id: '{}'", playlistId, userId);
        return authServiceClient.getYoutubeAccessToken(userId)
                .flatMap(accessToken -> youtubeApiClient.getPlaylistTracks(playlistId, accessToken));
    }

    @PostMapping("/analyze-track")
    public Mono<com.justice.ytmusic_service.model.TrackAnalysisResult> analyzeTrack(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody com.justice.ytmusic_service.model.TrackAnalysisRequest request) {
        return authServiceClient.getYoutubeAccessToken(userId)
                .flatMap(accessToken -> trackMatchingService.analyzeTrack(request, accessToken));
    }

    @PostMapping("/playlists/create")
    public Mono<com.justice.ytmusic_service.model.CreatePlaylistResponse> createPlaylist(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody com.justice.ytmusic_service.model.CreatePlaylistRequest request) {
        log.info("==> [YtmusicController] Creating YouTube playlist '{}' for user '{}'", request.getName(), userId);
        return authServiceClient.getYoutubeAccessToken(userId)
                .flatMap(accessToken -> youtubeApiClient.createPlaylist(request.getName(), request.getDescription(), accessToken));
    }

    @PostMapping("/playlists/{playlistId}/add-tracks")
    public Mono<Boolean> addTracksToPlaylist(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable("playlistId") String playlistId,
            @RequestBody com.justice.ytmusic_service.model.AddTracksRequest request) {
        log.info("==> [YtmusicController] Adding {} tracks to YouTube playlist '{}' for user '{}'",
                request.getVideoIds() != null ? request.getVideoIds().size() : 0, playlistId, userId);
        return authServiceClient.getYoutubeAccessToken(userId)
                .flatMap(accessToken -> youtubeApiClient.addTracksToPlaylist(playlistId, request.getVideoIds(), accessToken));
    }

    @PostMapping({"/execute/transfer", "/transfer/execute", "/execute"})
    public Mono<Object> executeTransfer(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestBody(required = false) Object requestBody) {
        log.info("==> [YtmusicController] Forwarding execute transfer request to transfer-service for user '{}'", userId);
        return transferServiceClient.forwardExecuteTransfer(userId, requestBody);
    }
}