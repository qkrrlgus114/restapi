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

    // 챗봇 API
    @PostMapping("gpt/recommendations")
    public ResponseEntity<ApiResponse<?>> chatGpt(@Valid @RequestBody ApiRequestDTO dto){
        ChatGPTResponseDTO chatGPTResponseDTO = apiRequestService.chatGpt(dto);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccess(chatGPTResponseDTO, "굿"));
    }

    // API 요청 이력 조회
    @GetMapping("admin/requests")
    public ResponseEntity<ApiResponse<?>> getApiRequestHistory(@RequestParam(value = "page", defaultValue = "0") int pageNumber){
        int pageSize = 10; // 한 페이지에 보여줄 요청 이력 수

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<RequestHistoryResponseDTO> apiRequestHistory = apiRequestService.getApiRequestHistory(pageNumber, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccess(apiRequestHistory, "요청 이력 조회 성공"));
    }

}
