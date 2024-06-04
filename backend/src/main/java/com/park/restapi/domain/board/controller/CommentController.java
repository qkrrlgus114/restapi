package com.park.restapi.domain.board.controller;

import com.park.restapi.domain.board.dto.request.CommentRequestDTO;
import com.park.restapi.domain.board.dto.response.CommentResponseDTO;
import com.park.restapi.domain.board.service.CommentService;
import com.park.restapi.util.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성하기
    @PostMapping("/post/{id}/comment")
    public ResponseEntity<ApiResponse<?>> addCommentPost(@PathVariable("id") Long postId, @RequestBody @Valid CommentRequestDTO commentRequestDTO) {

        CommentResponseDTO commentResponseDTO = commentService.addComment(postId, commentRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.createSuccess(commentResponseDTO, "댓글 작성 완료."));
    }
}
