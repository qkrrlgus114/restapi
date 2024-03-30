package com.park.restapi.domain.api.controller;

import com.park.restapi.domain.api.dto.request.ApiRequestDTO;
import com.park.restapi.domain.api.dto.response.ChatGPTResponseDTO;
import com.park.restapi.domain.api.service.impl.ApiRequestServiceImpl;
import com.park.restapi.util.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiRequestController {
    private final ApiRequestServiceImpl apiRequestService;

    @PostMapping("gpt/recommendations")
    public ResponseEntity<ApiResponse<?>> chatGpt(@RequestBody ApiRequestDTO dto){
        ChatGPTResponseDTO chatGPTResponseDTO = apiRequestService.chatGpt(dto);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccess(chatGPTResponseDTO, "굿"));
    }

}
