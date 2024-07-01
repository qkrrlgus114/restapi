package com.park.restapi.domain.board.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ApiRecommendPostsListResponseDTO(
        List<ApiRecommendPostsResponseDTO> apiRecommendPostsResponseDTOS,
        int totalPages,
        int currentPage
) {
}
