package com.park.restapi.domain.api.service;

import com.park.restapi.domain.api.dto.request.ApiRequestDTO;
import com.park.restapi.domain.api.dto.response.ChatGPTResponseDTO;
import com.park.restapi.domain.api.dto.response.RequestHistoryResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApiRequestService {
    // 챗봇 API
    ChatGPTResponseDTO chatGpt(ApiRequestDTO dto);
    // API 요청 이력 조회
    Page<RequestHistoryResponseDTO> getApiRequestHistory(int pageNumber, Pageable pageable);

}
