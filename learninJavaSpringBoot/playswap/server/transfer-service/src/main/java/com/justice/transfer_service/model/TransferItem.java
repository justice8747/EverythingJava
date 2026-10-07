package com.justice.transfer_service.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "transfer_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "job_id")
    private UUID jobId;

    @Column(name = "source_track_id", nullable = false)
    private String sourceTrackId;

    @Column(name = "target_track_id")
    private String targetTrackId;

    private String status;

    @Column(name = "confidence_score")
    private Integer confidenceScore;
}
