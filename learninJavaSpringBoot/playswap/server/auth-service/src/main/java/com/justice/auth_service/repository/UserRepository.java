package com.justice.auth_service.repository;

import com.justice.auth_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findBySpotifyId(String spotifyId);

    Optional<User> findByYoutubeId(String youtubeId);

    Optional<User> findByEmail(String email);
}