package com.justice.transfer_service.repository;

import com.justice.transfer_service.model.TransferSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransferSessionRepository extends JpaRepository<TransferSession, UUID> {
    List<TransferSession> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
