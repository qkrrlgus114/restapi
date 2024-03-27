package com.park.restapi.domain.api.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatGPTRequestDTO {

    private String model;
    private List<Message> messages;
    private int temperature = 1;
    private int maxTokens = 256;
    private int topP = 1;
    private int frequencyPenalty = 0;
    private int presencePenalty = 0;

    @Builder
    public ChatGPTRequestDTO(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
    }
}
