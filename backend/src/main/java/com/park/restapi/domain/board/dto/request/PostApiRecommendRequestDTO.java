package com.park.restapi.domain.board.dto.request;

import com.park.restapi.domain.board.entity.BoardType;
import com.park.restapi.domain.board.entity.Post;
import com.park.restapi.domain.member.entity.Member;
import jakarta.validation.constraints.NotBlank;

public record PostApiRecommendRequestDTO(

        @NotBlank(message = "content가 존재하지 않습니다.")
        String content,
        @NotBlank(message = "API 데이터가 존재하지 않습니다.")
        String apiContents
) {
    public Post toEntity(Member member, BoardType boardType) {
        return Post.builder()
                .member(member)
                .title(content)
                .boardType(boardType)
                .content(apiContents)
                .build();
    }
}
