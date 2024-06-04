package com.park.restapi.domain.board.controller;

import com.park.restapi.domain.board.dto.request.PostApiRecommendRequestDTO;
import com.park.restapi.domain.board.service.PostService;
import com.park.restapi.util.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class PostController {

    private final PostService postService;

    // 게시글 공유하기
    @PostMapping("/post/shard-api")
    public ResponseEntity<ApiResponse<?>> sharedGptApiRecommendPost(@Valid @RequestBody PostApiRecommendRequestDTO postApiRecommendRequestDTO) {

        postService.apiRecommendDataPost(postApiRecommendRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.createSuccessNoContent("api 추천 서비스 게시글 공유하기 완료."));
    }


}
