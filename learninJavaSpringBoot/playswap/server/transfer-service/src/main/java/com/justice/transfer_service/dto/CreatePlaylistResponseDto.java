package com.justice.transfer_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePlaylistResponseDto {
    private String playlistId;
    private String name;
    private String playlistUrl;
}
