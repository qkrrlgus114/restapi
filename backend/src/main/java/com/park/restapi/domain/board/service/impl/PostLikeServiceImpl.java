package com.park.restapi.domain.board.service.impl;

import com.park.restapi.domain.board.entity.Post;
import com.park.restapi.domain.board.entity.PostLike;
import com.park.restapi.domain.board.repository.PostLikeRepository;
import com.park.restapi.domain.board.repository.PostRepository;
import com.park.restapi.domain.board.service.PostLikeService;
import com.park.restapi.domain.exception.exception.MemberException;
import com.park.restapi.domain.exception.exception.PostException;
import com.park.restapi.domain.exception.exception.PostLikeException;
import com.park.restapi.domain.exception.info.MemberExceptionInfo;
import com.park.restapi.domain.exception.info.PostExceptionInfo;
import com.park.restapi.domain.exception.info.PostLikeExceptionInfo;
import com.park.restapi.domain.member.entity.Member;
import com.park.restapi.domain.member.repository.MemberRepository;
import com.park.restapi.util.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeServiceImpl implements PostLikeService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    // 좋아요 누르기
    @Override
    @Transactional
    public void likePost(Long postId) {
        Member currentMember = getCurrentMember();
        Post post = postRepository.findByIdWriteLockFetchJoinMember(postId)
                .orElseThrow(() -> new PostException(PostExceptionInfo.NOT_FOUND_POST, postId + "번 게시글을 찾지 못했습니다."));

        if (post.getMember().equals(currentMember)) {
            throw new PostLikeException(PostLikeExceptionInfo.DO_NOT_SELF_LIKE, currentMember.getId() + "번 유저가 본인의 게시글 좋아요를 눌렀습니다.");
        }

        if (postLikeRepository.existsByMemberAndPost(currentMember, post)) {
            throw new PostLikeException(PostLikeExceptionInfo.ALREADY_LIKE_POST, "이미 " + postId + "번 게시글 좋아요를 눌렀습니다.");
        }

        try {
            PostLike postLike = PostLike.builder()
                    .post(post)
                    .member(currentMember).build();
            postLikeRepository.save(postLike);

            post.incrementLikeCount();
        } catch (DataIntegrityViolationException e) {
            throw new PostLikeException(PostLikeExceptionInfo.ALREADY_LIKE_POST, "이미 " + postId + "번 게시글 좋아요를 눌렀습니다.");
        }

    }

    // 좋아요 취소하기
    @Override
    @Transactional
    public void unlikePost(Long postId) {
        Member currentMember = getCurrentMember();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostExceptionInfo.NOT_FOUND_POST, postId + "번 게시글을 찾지 못했습니다."));

        Optional<PostLike> byMemberAndPost = postLikeRepository.findByMemberAndPost(currentMember, post);
        if (byMemberAndPost.isEmpty()) {
            throw new PostLikeException(PostLikeExceptionInfo.DO_NOT_LIKE_POST, post + "번 게시글의 좋아요를 누르지 않았습니다.(좋아요 취소)");
        }

        // 좋아요 취소
        postLikeRepository.delete(byMemberAndPost.get());

        post.decrementLikeCount();
    }

    // 현재 로그인 유저 찾기
    private Member getCurrentMember() {
        Long currentUserId = jwtService.getCurrentUserId();
        return memberRepository.findById(currentUserId)
                .orElseThrow(() -> new MemberException(MemberExceptionInfo.NOT_FOUND_MEMBER, currentUserId + "번 유저를 찾지 못했습니다."));
    }

}
