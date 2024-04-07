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
    private Long no;
    private Long memberId;
    private LocalDateTime requestDate;
    private MethodType methodType;
    private String requestContent;
    private String responseContent;

    @Builder
    public RequestHistoryResponseDTO(Long no, Long memberId, LocalDateTime requestDate, MethodType methodType, String requestContent, String responseContent) {
        this.no = no;
        this.memberId = memberId;
        this.requestDate = requestDate;
        this.methodType = methodType;
        this.requestContent = requestContent;
        this.responseContent = responseContent;
    }
}
