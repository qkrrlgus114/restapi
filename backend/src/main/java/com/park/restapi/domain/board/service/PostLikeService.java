package com.park.restapi.domain.board.service;

import com.park.restapi.domain.member.entity.Member;

public interface PostLikeService {

    // 좋아요 누르기
    void likePost(Long postId, Member member);

    // 좋아요 취소하기
    void unlikePost(Long postId, Member member);
}
