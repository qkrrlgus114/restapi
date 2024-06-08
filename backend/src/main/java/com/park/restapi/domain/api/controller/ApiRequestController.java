package com.park.restapi.domain.api.controller;

import com.park.restapi.domain.api.dto.request.ApiRequestDTO;
import com.park.restapi.domain.api.dto.response.ChatGPTResponseDTO;
import com.park.restapi.domain.api.service.ApiRequestService;
import com.park.restapi.util.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gpt")
@Slf4j
public class ApiRequestController {

    private final ApiRequestService apiRequestService;

    // 챗봇 API
    @PostMapping("/recommendations")
    public ResponseEntity<ApiResponse<ChatGPTResponseDTO>> chatGpt(@Valid @RequestBody ApiRequestDTO apiRequestDTO) {
        ChatGPTResponseDTO chatGPTResponseDTO = apiRequestService.chatGpt(apiRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.createSuccess(chatGPTResponseDTO, "REST API 추천 완료."));
    }

}
