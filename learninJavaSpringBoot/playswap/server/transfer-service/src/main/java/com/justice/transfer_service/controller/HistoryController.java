package com.justice.transfer_service.controller;

import com.justice.transfer_service.dto.TransferSessionHistoryDto;
import com.justice.transfer_service.service.TransferHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class HistoryController {

    private final TransferHistoryService transferHistoryService;

    @GetMapping
    public Mono<List<TransferSessionHistoryDto>> getHistory(
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        if (userId == null || userId.isBlank()) {
            log.error("Rejecting /api/history request: Missing X-User-Id header");
            return Mono.error(new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED, "Missing required X-User-Id header."));
        }
        log.info("Received /api/history request for user {}", userId);
        return Mono.just(transferHistoryService.getUserTransferHistory(userId));
    }
}
