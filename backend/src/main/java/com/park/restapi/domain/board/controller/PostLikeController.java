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
@RequestMapping("/api/posts")
@Slf4j
public class PostLikeController {

    private final PostLikeService postLikeService;

    // 게시글 좋아요 누르기
    @PostMapping("/{id}/likes")
    public ResponseEntity<ApiResponse<Void>> likePost(@PathVariable("id") Long postId) {

        postLikeService.likePost(postId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 게시글 좋아요 취소하기
    @DeleteMapping("/{id}/likes")
    public ResponseEntity<ApiResponse<Void>> unlikePost(@PathVariable("id") Long postId) {

        postLikeService.unlikePost(postId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
