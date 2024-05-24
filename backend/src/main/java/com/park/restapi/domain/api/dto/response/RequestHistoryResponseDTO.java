package com.park.restapi.domain.api.dto.response;

import com.park.restapi.domain.api.entity.MethodType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// API 요청 이력 조회 응답 DTO
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestHistoryResponseDTO {

    private Long memberId;

    private LocalDateTime requestDate;

    private String email;

    private MethodType methodType;

    private String requestContent;

    private String responseContent;

    @Builder
    public RequestHistoryResponseDTO(Long memberId, LocalDateTime requestDate, String email, MethodType methodType, String requestContent, String responseContent) {
        this.memberId = memberId;
        this.requestDate = requestDate;
        this.email = email;
        this.methodType = methodType;
        this.requestContent = requestContent;
        this.responseContent = responseContent;
    }
}
