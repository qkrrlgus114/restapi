package com.park.restapi.domain.board.service;

import com.park.restapi.domain.board.dto.request.PostApiRecommendRequestDTO;

public interface PostService {

    // api 요청 데이터 등록하기
    void apiRecommendDataPost(PostApiRecommendRequestDTO postApiRecommendRequestDTO);
}
