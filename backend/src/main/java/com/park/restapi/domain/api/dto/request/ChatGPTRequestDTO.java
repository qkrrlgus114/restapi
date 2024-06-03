package com.park.restapi.domain.api.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ChatGPTRequestDTO(
        String model,
        List<Message> messages,
        int temperature,
        int maxTokens,
        int topP,
        int frequencyPenalty,
        int presencePenalty
) {
    @Builder
    public ChatGPTRequestDTO(String model, List<Message> messages) {
        this(model, messages, 1, 256, 1, 0, 0);
    }
}
