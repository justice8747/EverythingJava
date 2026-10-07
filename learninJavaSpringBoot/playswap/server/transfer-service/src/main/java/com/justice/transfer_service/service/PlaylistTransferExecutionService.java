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

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaylistTransferExecutionService {

    private final TransferSessionRepository sessionRepository;
    private final TransferJobRepository jobRepository;
    private final TransferItemRepository itemRepository;
    private final TransferHistoryRepository transferHistoryRepository;
    private final SpotifyServiceClient spotifyServiceClient;
    private final YtMusicServiceClient ytMusicServiceClient;

    public Mono<ExecuteTransferResponse> executeTransfer(String userIdStr, ExecuteTransferRequest request) {
        log.info("Starting execution of transfer for session {} by user {}", request.getSessionId(), userIdStr);

        TransferSession session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new IllegalArgumentException("Transfer session not found: " + request.getSessionId()));

        List<TransferJob> jobs = jobRepository.findBySessionId(session.getId());
        if (jobs == null || jobs.isEmpty()) {
            return Mono.error(new IllegalArgumentException("No transfer jobs found for session: " + session.getId()));
        }

        String targetPlatform = session.getTargetPlatform() != null ? session.getTargetPlatform().toUpperCase() : "YTMUSIC";
        List<TransferredPlaylistDto> transferredPlaylists = Collections.synchronizedList(new ArrayList<>());
        AtomicInteger grandTotalTracks = new AtomicInteger(0);

        return Flux.fromIterable(jobs)
                .concatMap(job -> {
                    List<String> targetTrackIds;
                    if (request.getJobTrackSelections() != null && request.getJobTrackSelections().containsKey(job.getId())) {
                        targetTrackIds = request.getJobTrackSelections().get(job.getId());
                    } else {
                        List<TransferItem> items = itemRepository.findByJobId(job.getId());
                        targetTrackIds = items.stream()
                                .filter(item -> "SUCCESS".equalsIgnoreCase(item.getStatus()) && item.getTargetTrackId() != null && !item.getTargetTrackId().isBlank())
                                .map(TransferItem::getTargetTrackId)
                                .distinct()
                                .collect(Collectors.toList());
                    }

                    if (targetTrackIds == null || targetTrackIds.isEmpty()) {
                        log.warn("No matched tracks to transfer for job {} (Playlist: {})", job.getId(), job.getSourcePlaylistName());
                        job.setStatus("TRANSFERRED_EMPTY");
                        jobRepository.save(job);
                        return Mono.empty();
                    }

                    String playlistName = (job.getSourcePlaylistName() != null ? job.getSourcePlaylistName() : "Playlist") + " (PlaySwap)";
                    String description = "Transferred via PlaySwap from " + session.getSourcePlatform() + " to " + session.getTargetPlatform();

                    Mono<CreatePlaylistResponseDto> createPlaylistMono;
                    if (targetPlatform.contains("SPOTIFY")) {
                        createPlaylistMono = spotifyServiceClient.createPlaylist(userIdStr, playlistName, description);
                    } else {
                        createPlaylistMono = ytMusicServiceClient.createPlaylist(userIdStr, playlistName, description);
                    }

                    return createPlaylistMono
                            .flatMap(created -> {
                                Mono<Boolean> addTracksMono;
                                if (targetPlatform.contains("SPOTIFY")) {
                                    addTracksMono = spotifyServiceClient.addTracksToPlaylist(userIdStr, created.getPlaylistId(), targetTrackIds);
                                } else {
                                    addTracksMono = ytMusicServiceClient.addTracksToPlaylist(userIdStr, created.getPlaylistId(), targetTrackIds);
                                }

                                return addTracksMono
                                        .map(success -> {
                                            job.setStatus("COMPLETED");
                                            jobRepository.save(job);

                                            grandTotalTracks.addAndGet(targetTrackIds.size());
                                            TransferredPlaylistDto dto = TransferredPlaylistDto.builder()
                                                    .jobId(job.getId())
                                                    .playlistName(created.getName() != null ? created.getName() : playlistName)
                                                    .targetPlaylistId(created.getPlaylistId())
                                                    .targetPlaylistUrl(created.getPlaylistUrl())
                                                    .transferredTracksCount(targetTrackIds.size())
                                                    .build();
                                            transferredPlaylists.add(dto);

                                            // Save record to transfer_history table
                                            try {
                                                UUID userUuid = session.getUserId();
                                                if (userUuid == null && userIdStr != null) {
                                                    userUuid = UUID.fromString(userIdStr);
                                                }
                                                if (userUuid != null) {
                                                    TransferHistory historyRecord = TransferHistory.builder()
                                                            .userId(userUuid)
                                                            .sessionId(session.getId())
                                                            .jobId(job.getId())
                                                            .sourcePlatform(session.getSourcePlatform())
                                                            .targetPlatform(session.getTargetPlatform())
                                                            .sourcePlaylistId(job.getSourcePlaylistId())
                                                            .sourcePlaylistName(job.getSourcePlaylistName())
                                                            .targetPlaylistId(created.getPlaylistId())
                                                            .targetPlaylistName(dto.getPlaylistName())
                                                            .targetPlaylistUrl(created.getPlaylistUrl())
                                                            .totalTracks(itemRepository.findByJobId(job.getId()).size())
                                                            .transferredTracksCount(targetTrackIds.size())
                                                            .status("COMPLETED")
                                                            .createdAt(java.time.LocalDateTime.now())
                                                            .build();
                                                    transferHistoryRepository.save(historyRecord);
                                                    log.info("Saved transfer record to transfer_history table for user {} - Playlist '{}'", userIdStr, dto.getPlaylistName());
                                                }
                                            } catch (Exception e) {
                                                log.error("Failed to save record to transfer_history table: {}", e.getMessage());
                                            }

                                            log.info("Successfully transferred {} tracks to target playlist '{}' ({})", targetTrackIds.size(), dto.getPlaylistName(), dto.getTargetPlaylistUrl());
                                            return dto;
                                        });
                            });
                })
                .then(Mono.defer(() -> {
                    session.setStatus("COMPLETED");
                    sessionRepository.save(session);

                    return Mono.just(ExecuteTransferResponse.builder()
                            .sessionId(session.getId())
                            .status("COMPLETED")
                            .totalPlaylists(transferredPlaylists.size())
                            .totalTransferredTracks(grandTotalTracks.get())
                            .transferredPlaylists(transferredPlaylists)
                            .build());
                }));
    }
}
