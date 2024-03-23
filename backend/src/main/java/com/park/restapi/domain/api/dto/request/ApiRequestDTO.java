package com.park.restapi.domain.api.dto.request;

import com.park.restapi.domain.api.entity.MethodType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApiRequestDTO {
    // 선택 모델
    private String model;
    // 어떤 작업인지(생성, 읽기, 업데이트, 삭제)
    private String operation;
    // Http 메서드 타입
    private MethodType methodType;
    // 주체가 되는 자원
    private String resource;
    // 간단한 설명
    private String content;
}
