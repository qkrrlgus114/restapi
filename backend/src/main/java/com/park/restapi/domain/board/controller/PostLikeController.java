package com.park.restapi.domain.board.controller;

import com.park.restapi.domain.board.service.PostLikeService;
import com.park.restapi.util.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class PostLikeController {

    private final PostLikeService postLikeService;

    // 게시글 좋아요 누르기
    @PostMapping("/post/{id}/like")
    public ResponseEntity<ApiResponse<?>> likePost(@PathVariable("id") Long postId) {

        postLikeService.likePost(postId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.createSuccessNoContent("좋아요 누르기 성공."));
    }

    // 게시글 좋아요 취소하기
    @DeleteMapping("/post/{id}/like")
    public ResponseEntity<ApiResponse<?>> unlikePost(@PathVariable("id") Long postId) {

        postLikeService.unlikePost(postId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.createSuccessNoContent("좋아요 취소하기 성공."));
    }

}
