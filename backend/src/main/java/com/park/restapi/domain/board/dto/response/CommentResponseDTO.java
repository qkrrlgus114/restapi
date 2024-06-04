package com.park.restapi.domain.board.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentResponseDTO(
        String comment,
        String email,
        LocalDateTime createDate
) {
}
