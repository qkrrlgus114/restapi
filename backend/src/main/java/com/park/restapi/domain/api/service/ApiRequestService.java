package com.park.restapi.domain.api.service;

import com.park.restapi.domain.api.dto.request.ApiRequestDTO;
import com.park.restapi.domain.api.dto.response.ApiRequestHistoryListResponseDTO;
import com.park.restapi.domain.api.dto.response.ChatGPTResponseDTO;

public interface ApiRequestService {
    // 챗봇 API
    ChatGPTResponseDTO chatGpt(ApiRequestDTO dto);
    // API 요청 기록 조회
    ApiRequestHistoryListResponseDTO getApiRequestHistory(int page, String searchType, String keyword);

}
