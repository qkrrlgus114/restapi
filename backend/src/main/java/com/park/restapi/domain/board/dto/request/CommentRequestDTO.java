package com.park.restapi.domain.board.dto.request;

import com.park.restapi.domain.board.entity.Comment;
import com.park.restapi.domain.board.entity.Post;
import com.park.restapi.domain.member.entity.Member;
import jakarta.validation.constraints.NotBlank;

public record CommentRequestDTO(
        @NotBlank(message = "댓글 내용을 입력해주세요")
        String comment
) {

    public Comment toEntity(Post post, Member member) {
        return Comment.builder()
                .post(post)
                .member(member)
                .content(comment).build();
    }
}
