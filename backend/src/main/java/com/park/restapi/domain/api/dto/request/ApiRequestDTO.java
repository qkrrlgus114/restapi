package com.park.restapi.domain.api.dto.request;

import com.park.restapi.domain.api.dto.response.ChatGPTResponseDTO;
import com.park.restapi.domain.api.entity.ApiRequestHistory;
import com.park.restapi.domain.api.entity.MethodType;
import com.park.restapi.domain.member.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApiRequestDTO {
    // 선택 모델
    @NotBlank(message = "모델을 선택해주세요.")
    private String model;
    // Http 메서드 타입
    @NotNull(message = "메서드 타입을 선택해주세요.")
    private MethodType methodType;
    // 주체가 되는 자원
    @NotBlank(message = "자원을 입력해주세요.")
    private String resource;
    // 간단한 설명
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    public ApiRequestHistory toEntity(ChatGPTResponseDTO responseDTO, Member member, boolean access){
        return ApiRequestHistory.builder()
                .member(member)
                .requestStatus(access)
                .requestContent(content)
                .methodType(methodType)
                .responseContent(responseDTO != null ? responseDTO.getChoices().get(0).getMessage().getContent() : null)
                .build();
    }
}
