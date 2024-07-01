package com.park.restapi.domain.api.dto.response;

import com.park.restapi.domain.api.entity.ApiRequestHistory;
import com.park.restapi.domain.api.entity.MethodType;
import lombok.Builder;

import java.time.LocalDateTime;

// API 요청 이력 조회 응답 DTO
@Builder
public record ApiRequestHistoryResponseDTO(
        Long memberId,
        LocalDateTime requestDate,
        String email,
        MethodType methodType,
        String requestContent,
        String responseContent
) {
    public static ApiRequestHistoryResponseDTO toDTO(ApiRequestHistory apiRequestHistory) {
        return ApiRequestHistoryResponseDTO.builder()
                .memberId(apiRequestHistory.getMember().getId())
                .requestDate(apiRequestHistory.getRequestDate())
                .email(apiRequestHistory.getMember().getEmail())
                .methodType(apiRequestHistory.getMethodType())
                .requestContent(apiRequestHistory.getRequestContent())
                .responseContent(apiRequestHistory.getResponseContent())
                .build();
    }
}
