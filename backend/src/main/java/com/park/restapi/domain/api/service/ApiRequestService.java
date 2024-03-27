package com.park.restapi.domain.api.service;

import com.park.restapi.domain.api.dto.request.ApiRequestDTO;
import com.park.restapi.domain.api.dto.response.ChatGPTResponseDTO;

public interface ApiRequestService {
    ChatGPTResponseDTO chatGpt(ApiRequestDTO dto);
}
