package com.park.restapi.domain.board.service.impl;

import com.park.restapi.domain.board.dto.request.PostApiRecommendRequestDTO;
import com.park.restapi.domain.board.entity.BoardType;
import com.park.restapi.domain.board.entity.Post;
import com.park.restapi.domain.board.repository.PostRepository;
import com.park.restapi.domain.board.service.PostService;
import com.park.restapi.domain.exception.exception.MemberException;
import com.park.restapi.domain.exception.info.MemberExceptionInfo;
import com.park.restapi.domain.member.entity.Member;
import com.park.restapi.domain.member.repository.MemberRepository;
import com.park.restapi.util.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    // api 응답 공유게시판 등록
    @Override
    @Transactional
    public void apiRecommendDataPost(PostApiRecommendRequestDTO postApiRecommendRequestDTO) {
        Member currentMember = getCurrentMember();

        // 게시글 생성
        Post post = postApiRecommendRequestDTO.toEntity(currentMember, BoardType.SHARE);
        postRepository.save(post);
    }

    // 현재 로그인 유저 찾기
    private Member getCurrentMember() {
        Long currentUserId = jwtService.getCurrentUserId();
        return memberRepository.findById(currentUserId)
                .orElseThrow(
                        () -> new MemberException(MemberExceptionInfo.NOT_FOUND_MEMBER, currentUserId + "번 유저를 찾지 못했습니다."));
    }
}
