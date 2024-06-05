package com.park.restapi.domain.board.dto.response;

import com.park.restapi.domain.api.entity.MethodType;
import lombok.Builder;

@Builder
public record ApiRecommendPostsResponseDTO(
        Long postId,
        MethodType methodType,
        String title,
        String nickname,
        Integer likeCount,
        Integer viewCount
) {
}
