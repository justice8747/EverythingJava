package com.justice.transfer_service.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transfer_jobs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferJob {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "session_id")
    private UUID sessionId;

    @Column(name = "source_playlist_id", nullable = false)
    private String sourcePlaylistId;

    @Column(name = "source_playlist_name")
    private String sourcePlaylistName;

    private String status;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
