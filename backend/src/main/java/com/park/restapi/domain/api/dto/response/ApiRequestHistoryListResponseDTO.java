package com.park.restapi.domain.api.dto.response;

import com.park.restapi.domain.inquiry.dto.response.InquiryResponseDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiRequestHistoryListResponseDTO {

    private List<ApiRequestHistoryResponseDTO> apiRequestHistoryResponseDTOS;
    private int totalPages;
    private int currentPage;

    @Builder
    public ApiRequestHistoryListResponseDTO(List<ApiRequestHistoryResponseDTO> apiRequestHistoryResponseDTOS, int totalPages, int currentPage) {
        this.apiRequestHistoryResponseDTOS = apiRequestHistoryResponseDTOS;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
    }
}
