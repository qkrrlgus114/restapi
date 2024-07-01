package com.park.restapi.domain.board.dto.request;

import com.park.restapi.domain.api.entity.MethodType;
import com.park.restapi.domain.board.entity.BoardType;
import com.park.restapi.domain.board.entity.Post;
import com.park.restapi.domain.member.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ApiRecommendPostRequestDTO(

        @NotBlank(message = "content가 존재하지 않습니다.")
        String title,
        @NotBlank(message = "API 데이터가 존재하지 않습니다.")
        String apiContents,
        @NotNull(message = "메서드 타입이 존재하지 않습니다.")
        MethodType methodType
) {
    public Post toEntity(Member member, BoardType boardType, MethodType methodType) {
        return Post.builder()
                .member(member)
                .title(title)
                .methodType(methodType)
                .boardType(boardType)
                .content(apiContents)
                .build();
    }
}
