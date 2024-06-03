package com.park.restapi.domain.api.dto.response;

import com.park.restapi.domain.api.dto.request.Message;

import java.util.List;

public record ChatGPTResponseDTO(List<Choice> choices) {

    public record Choice(int index, Message message) {
    }
}
