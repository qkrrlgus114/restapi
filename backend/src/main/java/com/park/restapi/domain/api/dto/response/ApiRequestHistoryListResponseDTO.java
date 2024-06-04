package com.park.restapi.domain.api.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ApiRequestHistoryListResponseDTO(
        List<ApiRequestHistoryResponseDTO> apiRequestHistoryResponseDTOS,
        int totalPages,
        int currentPage) {
}
