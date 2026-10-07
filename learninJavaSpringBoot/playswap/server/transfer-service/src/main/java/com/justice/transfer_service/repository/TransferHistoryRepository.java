package com.justice.transfer_service.repository;

import com.justice.transfer_service.model.TransferHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransferHistoryRepository extends JpaRepository<TransferHistory, UUID> {
    List<TransferHistory> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
