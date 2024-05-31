package com.park.restapi.domain.api.controller;

import com.park.restapi.domain.api.dto.request.ApiRequestDTO;
import com.park.restapi.domain.api.dto.response.ApiRequestHistoryListResponseDTO;
import com.park.restapi.domain.api.dto.response.ApiRequestHistoryResponseDTO;
import com.park.restapi.domain.api.dto.response.ChatGPTResponseDTO;
import com.park.restapi.domain.api.service.impl.ApiRequestServiceImpl;
import com.park.restapi.util.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class ApiRequestController {

    private final ApiRequestServiceImpl apiRequestService;

    // 챗봇 API
    @PostMapping("gpt/recommendations")
    public ResponseEntity<ApiResponse<?>> chatGpt(@Valid @RequestBody ApiRequestDTO dto){
        System.out.println(dto.getContent());
        ChatGPTResponseDTO chatGPTResponseDTO = apiRequestService.chatGpt(dto);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccess(chatGPTResponseDTO, "REST API 추천 완료."));
    }

    // API 요청 이력 조회
    @GetMapping("admin/requests")
    public ResponseEntity<ApiResponse<?>> getApiRequestHistory(@RequestParam(value = "page", defaultValue = "1") int page,
                                                               @RequestParam(value = "searchType", required = false) String searchType,
                                                                @RequestParam(value = "searchKey", required = false) String keyword){

        Long startTime = System.currentTimeMillis();
        ApiRequestHistoryListResponseDTO apiRequestHistory = apiRequestService.getApiRequestHistory(page - 1, searchType, keyword);
        Long endTime = System.currentTimeMillis();
        log.info("api 요청 이력 검색 시간 : {}", endTime - startTime);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccess(apiRequestHistory, "요청 이력 조회 성공"));
    }

}
