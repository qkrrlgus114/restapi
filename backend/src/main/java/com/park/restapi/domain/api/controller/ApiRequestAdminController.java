package com.park.restapi.domain.api.controller;

import com.park.restapi.domain.api.dto.response.ApiRequestHistoryListResponseDTO;
import com.park.restapi.domain.api.service.ApiRequestService;
import com.park.restapi.util.entity.SearchType;
import com.park.restapi.util.response.ApiResponse;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gpt/admin")
@Slf4j
public class ApiRequestAdminController {

    private final ApiRequestService apiRequestService;

    // API 요청 이력 조회
    @GetMapping("/requests/history")
    public ResponseEntity<ApiResponse<ApiRequestHistoryListResponseDTO>> getApiRequestHistory(
            @Min(value = 1, message = "페이지 검색은 1 페이지부터 가능합니다.") @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "searchType", required = false, defaultValue = "NONE") SearchType searchType,
            @RequestParam(value = "searchKey", required = false) String keyword) {

        ApiRequestHistoryListResponseDTO apiRequestHistory = apiRequestService.getApiRequestHistory(page - 1, searchType, keyword);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.createSuccess(apiRequestHistory, "요청 이력 조회 성공"));
    }
}
