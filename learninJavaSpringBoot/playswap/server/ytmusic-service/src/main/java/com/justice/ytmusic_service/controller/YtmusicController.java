package com.justice.ytmusic_service.controller;

import com.justice.ytmusic_service.client.AuthServiceClient;
import com.justice.ytmusic_service.client.YoutubeApiClient;
import com.justice.ytmusic_service.model.YoutubePlaylist;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/youtube")
@RequiredArgsConstructor
public class YtmusicController {

    private final AuthServiceClient authServiceClient;
    private final YoutubeApiClient youtubeApiClient;

    @GetMapping("/playlists")
    public Mono<List<YoutubePlaylist>> getPlaylists(
            @RequestHeader("X-User-Id") String userId) {

        return authServiceClient.getYoutubeAccessToken(userId)
                .flatMap(accessToken ->
                        youtubeApiClient.getUserPlaylists(accessToken));
    }
}