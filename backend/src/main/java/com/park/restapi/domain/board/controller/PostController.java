package com.park.restapi.domain.board.controller;

import com.park.restapi.domain.board.dto.request.ApiRecommendPostRequestDTO;
import com.park.restapi.domain.board.dto.response.ApiRecommendPostResponseDTO;
import com.park.restapi.domain.board.dto.response.ApiRecommendPostsListResponseDTO;
import com.park.restapi.domain.board.service.PostService;
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
public class PostController {

    private final PostService postService;

    // 게시글 공유하기
    @PostMapping("post/shard-api")
    public ResponseEntity<ApiResponse<Void>> sharedGptApiRecommendPost(@Valid @RequestBody ApiRecommendPostRequestDTO apiRecommendPostRequestDTO) {

        postService.apiRecommendDataPost(apiRecommendPostRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.createSuccessNoContent("api 추천 서비스 게시글 공유하기 완료."));
    }

    // 게시글 가져오기
    @GetMapping("post/shard-api")
    public ResponseEntity<ApiResponse<ApiRecommendPostsListResponseDTO>> getGptApiRecommendPost(@RequestParam(value = "page", defaultValue = "1") int page) {

        System.out.println("들어옴");

        ApiRecommendPostsListResponseDTO gptApiRecommendPosts = postService.getGptApiRecommendPosts(page - 1);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.createSuccess(gptApiRecommendPosts, "모든 공유 게시글 가져오기 성공"));
    }

    // 특정 게시글 가져오기
    @GetMapping("post/shard-api/{id}")
    public ResponseEntity<ApiResponse<ApiRecommendPostResponseDTO>> getGptApiRecommendPostTarget(@PathVariable("id") Long postId) {

        ApiRecommendPostResponseDTO gptApiRecommendPost = postService.getGptApiRecommendPost(postId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.createSuccess(gptApiRecommendPost, postId + "번 공유 게시글 가져오기 성공"));
    }


}
