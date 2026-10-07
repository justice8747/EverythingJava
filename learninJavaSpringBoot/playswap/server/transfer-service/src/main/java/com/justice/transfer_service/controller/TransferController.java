package com.justice.transfer_service.controller;

import com.justice.transfer_service.dto.AnalysisSummaryResponse;
import com.justice.transfer_service.dto.AnalyzePlaylistsRequest;
import com.justice.transfer_service.service.PlaylistAnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/transfer")
@RequiredArgsConstructor
public class TransferController {

    private final PlaylistAnalysisService playlistAnalysisService;
    private final com.justice.transfer_service.service.PlaylistTransferExecutionService playlistTransferExecutionService;
    private final com.justice.transfer_service.service.TransferHistoryService transferHistoryService;

    @PostMapping("/analyze")
    public Mono<AnalysisSummaryResponse> analyzePlaylists(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestBody(required = false) AnalyzePlaylistsRequest request) {
        if (userId == null || userId.isBlank()) {
            log.error("Rejecting analyze request: Missing X-User-Id header");
            return Mono.error(new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED, "Missing required X-User-Id header. Ensure request passes through API Gateway with a valid token."));
        }
        if (request == null || request.getPlaylistIds() == null || request.getPlaylistIds().isEmpty()) {
            log.error("Rejecting analyze request from user {}: Request body or playlistIds is null/empty", userId);
            return Mono.error(new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "Request body must contain non-empty 'playlistIds' list."));
        }
        if (request.getSourcePlatform() == null || request.getSourcePlatform().isBlank()) {
            request.setSourcePlatform("SPOTIFY");
        }
        if (request.getTargetPlatform() == null || request.getTargetPlatform().isBlank()) {
            request.setTargetPlatform("YTMUSIC");
        }
        log.info("Received analyze request from user {} for {} playlists (Source: {}, Target: {})",
                userId, request.getPlaylistIds().size(), request.getSourcePlatform(), request.getTargetPlatform());
        return playlistAnalysisService.analyzePlaylists(userId, request);
    }

    @PostMapping({"/execute", "/execute/transfer", "/transfer/execute"})
    public Mono<com.justice.transfer_service.dto.ExecuteTransferResponse> executeTransfer(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestBody(required = false) com.justice.transfer_service.dto.ExecuteTransferRequest request) {
        if (userId == null || userId.isBlank()) {
            log.error("Rejecting execute request: Missing X-User-Id header");
            return Mono.error(new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED, "Missing required X-User-Id header."));
        }
        if (request == null || request.getSessionId() == null) {
            log.error("Rejecting execute request from user {}: Request body or sessionId is null", userId);
            return Mono.error(new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "Request body must contain non-null 'sessionId'."));
        }
        log.info("Received execute transfer request from user {} for session {}", userId, request.getSessionId());
        return playlistTransferExecutionService.executeTransfer(userId, request);
    }

    @GetMapping({"/history", "/history/sessions", "/user/history"})
    public Mono<java.util.List<com.justice.transfer_service.dto.TransferSessionHistoryDto>> getTransferHistory(
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        if (userId == null || userId.isBlank()) {
            log.error("Rejecting history request: Missing X-User-Id header");
            return Mono.error(new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED, "Missing required X-User-Id header."));
        }
        log.info("Received transfer history request for user {}", userId);
        return Mono.just(transferHistoryService.getUserTransferHistory(userId));
    }
}
