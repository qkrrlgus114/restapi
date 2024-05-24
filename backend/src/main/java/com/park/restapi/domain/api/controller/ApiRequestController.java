package com.park.restapi.domain.api.controller;

import com.park.restapi.domain.api.dto.request.ApiRequestDTO;
import com.park.restapi.domain.api.dto.response.ChatGPTResponseDTO;
import com.park.restapi.domain.api.dto.response.RequestHistoryResponseDTO;
import com.park.restapi.domain.api.service.impl.ApiRequestServiceImpl;
import com.park.restapi.util.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiRequestController {
    private final ApiRequestServiceImpl apiRequestService;
    private final int PAGE_SIZE = 10; // 페이지 크기

    // 챗봇 API
    @PostMapping("gpt/recommendations")
    public ResponseEntity<ApiResponse<?>> chatGpt(@Valid @RequestBody ApiRequestDTO dto){
        System.out.println(dto.getContent());
        ChatGPTResponseDTO chatGPTResponseDTO = apiRequestService.chatGpt(dto);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccess(chatGPTResponseDTO, "REST API 추천 완료."));
    }

    // API 요청 이력 조회
    @GetMapping("admin/requests")
    public ResponseEntity<ApiResponse<?>> getApiRequestHistory(@RequestParam(value = "page", defaultValue = "0") int pageNumber,
                                                               @RequestParam(value = "searchType", required = false) String searchType,
                                                                @RequestParam(value = "searchKey", required = false) String keyword){
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);
        Page<RequestHistoryResponseDTO> apiRequestHistory = null;

        if(searchType != null && keyword != null && !searchType.isEmpty() && !keyword.isEmpty()){
            apiRequestHistory = apiRequestService.getApiRequestHistoryByCondition(pageable, searchType, keyword);
        }else{
            apiRequestHistory = apiRequestService.getApiRequestHistory(pageable);
        }

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccess(apiRequestHistory, "요청 이력 조회 성공"));
    }

}
