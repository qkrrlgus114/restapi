package com.park.restapi.domain.board.service;

public interface PostLikeService {

    // 좋아요 누르기
    void likePost(Long postId);

    // 좋아요 취소하기
    void unlikePost(Long postId);
}
