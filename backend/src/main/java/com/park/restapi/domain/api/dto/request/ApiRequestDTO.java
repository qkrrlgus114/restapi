package com.park.restapi.domain.api.dto.request;

import com.park.restapi.domain.api.dto.response.ChatGPTResponseDTO;
import com.park.restapi.domain.api.entity.ApiRequestHistory;
import com.park.restapi.domain.api.entity.MethodType;
import com.park.restapi.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApiRequestDTO {
    // 선택 모델
    private String model;
    // Http 메서드 타입
    private MethodType methodType;
    // 주체가 되는 자원
    private String resource;
    // 간단한 설명
    private String content;


    public ApiRequestHistory toEntity(ChatGPTResponseDTO responseDTO, User user, boolean access){
        return ApiRequestHistory.builder()
                .user(user)
                .request_status(access)
                .requestContent(content)
                .methodType(methodType)
                .responseContent(responseDTO != null ? responseDTO.getChoices().get(0).getMessage().getContent() : null)
                .build();
    }
}
