package com.park.restapi.domain.board.service;

import com.park.restapi.domain.board.dto.request.ApiRecommendPostRequestDTO;
import com.park.restapi.domain.board.dto.response.ApiRecommendPostResponseDTO;
import com.park.restapi.domain.board.dto.response.ApiRecommendPostsListResponseDTO;

public interface PostService {

    // api 요청 데이터 등록하기
    void apiRecommendDataPost(ApiRecommendPostRequestDTO apiRecommendPostRequestDTO);

    // 모든 공유 게시글 가져오기
    ApiRecommendPostsListResponseDTO getGptApiRecommendPosts(int page);

    // 특정 공유 게시글 가져오기
    ApiRecommendPostResponseDTO getGptApiRecommendPost(Long postId);
}
