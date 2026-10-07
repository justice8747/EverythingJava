package com.justice.transfer_service.repository;

import com.justice.transfer_service.model.SongMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SongMappingRepository extends JpaRepository<SongMapping, UUID> {
    Optional<SongMapping> findBySourcePlatformAndSourceTrackIdAndDestinationPlatform(
            String sourcePlatform, String sourceTrackId, String destinationPlatform);
}
