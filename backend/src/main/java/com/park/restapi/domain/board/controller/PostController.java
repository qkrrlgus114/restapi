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
@RequestMapping("/api/posts")
@Slf4j
public class PostController {

    private final PostService postService;

    // 게시글 공유하기
    @PostMapping()
    public ResponseEntity<ApiResponse<Void>> sharedGptApiRecommendPost(@Valid @RequestBody ApiRecommendPostRequestDTO apiRecommendPostRequestDTO) {
        postService.apiRecommendDataPost(apiRecommendPostRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.createSuccessNoContent("api 추천 서비스 게시글 공유하기 완료."));
    }

    // 모든 게시글 가져오기
    @GetMapping()
    public ResponseEntity<ApiResponse<ApiRecommendPostsListResponseDTO>> getGptApiRecommendPost(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "searchType", required = false, defaultValue = "") String searchType,
            @RequestParam(value = "searchKey", required = false, defaultValue = "") String searchKey,
            @RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy) {
        ApiRecommendPostsListResponseDTO apiRecommendPostsListResponseDTO = postService.getGptApiRecommendPosts(page - 1, searchType, searchKey, sortBy);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccess(apiRecommendPostsListResponseDTO, "모든 공유 게시글 가져오기 성공"));
    }

    // 특정 게시글 가져오기
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ApiRecommendPostResponseDTO>> getGptApiRecommendPostTarget(@PathVariable("id") Long postId) {
        ApiRecommendPostResponseDTO apiRecommendPostResponseDTO = postService.getGptApiRecommendPost(postId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccess(apiRecommendPostResponseDTO, postId + "번 공유 게시글 가져오기 성공"));
    }


}
