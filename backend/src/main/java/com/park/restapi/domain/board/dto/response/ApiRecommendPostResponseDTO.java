package com.park.restapi.domain.board.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ApiRecommendPostResponseDTO(
        Long postId,
        String title,
        String nickname,
        String content,
        LocalDateTime createdDate,
        Integer likeCount,
        Integer viewCount,
        boolean isLiked
) {
}
