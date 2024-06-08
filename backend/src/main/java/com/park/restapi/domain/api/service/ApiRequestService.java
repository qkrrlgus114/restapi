package com.park.restapi.domain.api.service;

import com.park.restapi.domain.api.dto.request.ApiRequestDTO;
import com.park.restapi.domain.api.dto.response.ApiRequestHistoryListResponseDTO;
import com.park.restapi.domain.api.dto.response.ChatGPTResponseDTO;
import com.park.restapi.domain.member.entity.Member;
import com.park.restapi.util.entity.SearchType;

public interface ApiRequestService {
    // 챗봇 API
    ChatGPTResponseDTO chatGpt(ApiRequestDTO dto, Member member);

    // API 요청 기록 조회
    ApiRequestHistoryListResponseDTO getApiRequestHistory(int page, SearchType searchType, String keyword, Member member);

}
