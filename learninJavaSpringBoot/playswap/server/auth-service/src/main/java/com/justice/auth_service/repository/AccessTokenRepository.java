package com.justice.auth_service.repository;

import com.justice.auth_service.entity.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, UUID> {

    Optional<AccessToken> findByUserIdAndPlatform(UUID userId, String platform);

    void deleteByUserIdAndPlatform(UUID userId, String platform);
}
