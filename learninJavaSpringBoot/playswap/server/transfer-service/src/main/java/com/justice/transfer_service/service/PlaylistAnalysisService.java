package com.justice.transfer_service.service;

import com.justice.transfer_service.client.SpotifyServiceClient;
import com.justice.transfer_service.client.YtMusicServiceClient;
import com.justice.transfer_service.dto.*;
import com.justice.transfer_service.model.*;
import com.justice.transfer_service.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaylistAnalysisService {

    private final TransferSessionRepository sessionRepository;
    private final TransferJobRepository jobRepository;
    private final TransferItemRepository itemRepository;
    private final SongMappingRepository songMappingRepository;
    private final SpotifyServiceClient spotifyServiceClient;
    private final YtMusicServiceClient ytMusicServiceClient;

    public Mono<AnalysisSummaryResponse> analyzePlaylists(String userIdStr, AnalyzePlaylistsRequest request) {
        log.info("Starting playlist analysis for user {} across {} playlists", userIdStr, request.getPlaylistIds().size());

        UUID userUuid = null;
        try {
            if (userIdStr != null) {
                userUuid = UUID.fromString(userIdStr);
            }
        } catch (IllegalArgumentException e) {
            log.warn("Could not parse userId to UUID: {}", userIdStr);
        }

        // 1. Create Transfer Session
        TransferSession session = TransferSession.builder()
                .userId(userUuid)
                .sourcePlatform(request.getSourcePlatform())
                .targetPlatform(request.getTargetPlatform())
                .status("PROCESSING")
                .build();
        session = sessionRepository.save(session);
        final UUID sessionId = session.getId();

        AtomicInteger totalTracksCount = new AtomicInteger(0);
        AtomicInteger totalMatchedCount = new AtomicInteger(0);
        AtomicInteger totalUnmatchedCount = new AtomicInteger(0);
        AtomicInteger totalCachedCount = new AtomicInteger(0);

        List<JobSummaryDto> jobSummaries = Collections.synchronizedList(new ArrayList<>());

        // 2. Process each playlist sequentially / concurrently
        return Flux.fromIterable(request.getPlaylistIds())
                .concatMap(playlistId -> {
                    String playlistName = request.getPlaylistNames() != null ?
                            request.getPlaylistNames().getOrDefault(playlistId, "Playlist " + playlistId) : "Playlist " + playlistId;

                    TransferJob job = TransferJob.builder()
                            .sessionId(sessionId)
                            .sourcePlaylistId(playlistId)
                            .sourcePlaylistName(playlistName)
                            .status("PROCESSING")
                            .build();
                    TransferJob savedJob = jobRepository.save(job);

                    Mono<List<SpotifyTrackDto>> tracksMono;
                    String sourcePlatform = request.getSourcePlatform() != null ? request.getSourcePlatform().toUpperCase() : "";
                    if (sourcePlatform.contains("YTMUSIC") || sourcePlatform.contains("YOUTUBE")) {
                        tracksMono = ytMusicServiceClient.getPlaylistTracks(userIdStr, playlistId);
                    } else {
                        tracksMono = spotifyServiceClient.getPlaylistTracks(userIdStr, playlistId);
                    }

                    return tracksMono
                            .flatMap(tracks -> {
                                log.info("Fetched {} tracks for playlist {} from source platform {}", tracks.size(), playlistName, request.getSourcePlatform());
                                AtomicInteger jobMatched = new AtomicInteger(0);
                                AtomicInteger jobUnmatched = new AtomicInteger(0);

                                return Flux.fromIterable(tracks)
                                        .flatMap(track -> processTrack(userIdStr, savedJob.getId(), request.getSourcePlatform(), request.getTargetPlatform(), track)
                                                .doOnNext(matched -> {
                                                    totalTracksCount.incrementAndGet();
                                                    if (matched) {
                                                        jobMatched.incrementAndGet();
                                                        totalMatchedCount.incrementAndGet();
                                                    } else {
                                                        jobUnmatched.incrementAndGet();
                                                        totalUnmatchedCount.incrementAndGet();
                                                    }
                                                }), 5) // Concurrency limit 5
                                        .then(Mono.fromRunnable(() -> {
                                            savedJob.setStatus("COMPLETED");
                                            jobRepository.save(savedJob);
                                            jobSummaries.add(JobSummaryDto.builder()
                                                    .jobId(savedJob.getId())
                                                    .playlistId(playlistId)
                                                    .playlistName(playlistName)
                                                    .totalTracks(tracks.size())
                                                    .matchedCount(jobMatched.get())
                                                    .unmatchedCount(jobUnmatched.get())
                                                    .build());
                                        }));
                            });
                })
                .then(Mono.defer(() -> {
                    TransferSession finalSession = sessionRepository.findById(sessionId).orElseThrow();
                    finalSession.setStatus("COMPLETED");
                    sessionRepository.save(finalSession);

                    return Mono.just(AnalysisSummaryResponse.builder()
                            .sessionId(sessionId)
                            .totalPlaylists(request.getPlaylistIds().size())
                            .totalTracks(totalTracksCount.get())
                            .matchedTracks(totalMatchedCount.get())
                            .unmatchedTracks(totalUnmatchedCount.get())
                            .cachedMatches(totalCachedCount.get())
                            .status("COMPLETED")
                            .jobs(jobSummaries)
                            .build());
                }));
    }

    private Mono<Boolean> processTrack(String userId, UUID jobId, String sourcePlatform, String targetPlatform, SpotifyTrackDto track) {
        // Check existing mapping database cache
        Optional<SongMapping> existingMapping = songMappingRepository
                .findBySourcePlatformAndSourceTrackIdAndDestinationPlatform(sourcePlatform, track.getTrackId(), targetPlatform);

        if (existingMapping.isPresent()) {
            SongMapping mapping = existingMapping.get();
            mapping.setLastUsedAt(LocalDateTime.now());
            songMappingRepository.save(mapping);

            TransferItem item = TransferItem.builder()
                    .jobId(jobId)
                    .sourceTrackId(track.getTrackId())
                    .targetTrackId(mapping.getDestinationTrackId())
                    .status("SUCCESS")
                    .confidenceScore(mapping.getConfidenceScore())
                    .build();
            itemRepository.save(item);
            return Mono.just(true);
        }

        // Search via target microservice
        Mono<TrackAnalysisResultDto> analysisMono;
        String target = targetPlatform != null ? targetPlatform.toUpperCase() : "";
        if (target.contains("SPOTIFY")) {
            analysisMono = spotifyServiceClient.analyzeTrack(userId, track);
        } else {
            analysisMono = ytMusicServiceClient.analyzeTrack(userId, track);
        }

        return analysisMono
                .map(result -> {
                    String matchedTargetId = null;
                    if (result != null && result.isMatched()) {
                        matchedTargetId = result.getSpotifyTrackId() != null ? result.getSpotifyTrackId() : result.getYoutubeVideoId();
                    }

                    if (matchedTargetId != null) {
                        // Save new song mapping
                        SongMapping newMapping = SongMapping.builder()
                                .sourcePlatform(sourcePlatform)
                                .sourceTrackId(track.getTrackId())
                                .destinationPlatform(targetPlatform)
                                .destinationTrackId(matchedTargetId)
                                .title(track.getTitle())
                                .artist(track.getArtists())
                                .confidenceScore(result.getConfidence())
                                .lastUsedAt(LocalDateTime.now())
                                .build();
                        songMappingRepository.save(newMapping);

                        TransferItem item = TransferItem.builder()
                                .jobId(jobId)
                                .sourceTrackId(track.getTrackId())
                                .targetTrackId(matchedTargetId)
                                .status("SUCCESS")
                                .confidenceScore(result.getConfidence())
                                .build();
                        itemRepository.save(item);
                        return true;
                    } else {
                        TransferItem item = TransferItem.builder()
                                .jobId(jobId)
                                .sourceTrackId(track.getTrackId())
                                .targetTrackId(null)
                                .status("UNMATCHED")
                                .confidenceScore(0)
                                .build();
                        itemRepository.save(item);
                        return false;
                    }
                })
                .onErrorReturn(false);
    }
}
