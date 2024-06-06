package com.park.restapi.domain.board.service.impl;

import com.park.restapi.domain.board.dto.request.ApiRecommendPostRequestDTO;
import com.park.restapi.domain.board.dto.response.ApiRecommendPostResponseDTO;
import com.park.restapi.domain.board.dto.response.ApiRecommendPostsListResponseDTO;
import com.park.restapi.domain.board.dto.response.ApiRecommendPostsResponseDTO;
import com.park.restapi.domain.board.entity.BoardType;
import com.park.restapi.domain.board.entity.Post;
import com.park.restapi.domain.board.entity.PostLike;
import com.park.restapi.domain.board.repository.PostLikeRepository;
import com.park.restapi.domain.board.repository.PostRepository;
import com.park.restapi.domain.board.service.PostService;
import com.park.restapi.domain.exception.exception.MemberException;
import com.park.restapi.domain.exception.exception.PostException;
import com.park.restapi.domain.exception.info.MemberExceptionInfo;
import com.park.restapi.domain.exception.info.PostExceptionInfo;
import com.park.restapi.domain.member.entity.Member;
import com.park.restapi.domain.member.repository.MemberRepository;
import com.park.restapi.util.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    private final static int DEFAULT_DATA_COUNT = 10;

    // api 응답 공유게시판 등록
    @Override
    @Transactional
    public void apiRecommendDataPost(ApiRecommendPostRequestDTO apiRecommendPostRequestDTO) {
        Member currentMember = getCurrentMember();

        // 게시글 생성
        Post post = apiRecommendPostRequestDTO.toEntity(currentMember, BoardType.SHARE, apiRecommendPostRequestDTO.methodType());
        postRepository.save(post);
    }

    // api 공유게시글 가져오기(페이지네이션)
    @Override
    @Transactional(readOnly = true)
    public ApiRecommendPostsListResponseDTO getGptApiRecommendPosts(int page, String searchType, String searchKey, String sortBy) {
        PageRequest pageRequest = PageRequest.of(page, DEFAULT_DATA_COUNT);

        Page<ApiRecommendPostsResponseDTO> apiRecommendPostsResponseDTOS = postRepository.findRecommendPosts(pageRequest, searchType, searchKey, sortBy);

        return ApiRecommendPostsListResponseDTO.builder()
                .apiRecommendPostsResponseDTOS(apiRecommendPostsResponseDTOS.getContent())
                .currentPage(apiRecommendPostsResponseDTOS.getNumber())
                .totalPages(apiRecommendPostsResponseDTOS.getTotalPages()).build();
    }

    // api 특정 공유게시글 가져오기
    @Override
    @Transactional
    public ApiRecommendPostResponseDTO getGptApiRecommendPost(Long postId) {
        Member currentMember = getCurrentMember();

        System.out.println("들어옴3");
        Post post = postRepository.findByIdWriteLockFetchJoinMember(postId)
                .orElseThrow(() -> new PostException(PostExceptionInfo.NOT_FOUND_POST, postId + "번 게시글이 존재하지 않습니다."));

        post.incrementViewCount();

        // 좋아요 여부 찾기
        boolean isLiked = false;
        if (currentMember != null) {
            Optional<PostLike> byMemberAndPost = postLikeRepository.findByMemberAndPost(currentMember, post);
            if (byMemberAndPost.isPresent()) isLiked = true;
        }

        return ApiRecommendPostResponseDTO.builder()
                .postId(post.getId()).nickname(post.getMember().getNickname()).title(post.getTitle()).content(post.getContent())
                .createdDate(post.getCreatedDate()).likeCount(post.getLikeCount()).viewCount(post.getViewCount()).isLiked(isLiked).build();
    }

    // 현재 로그인 유저 찾기
    private Member getCurrentMember() {
        Long currentUserId = jwtService.getCurrentUserId();
        if (currentUserId == null) {
            return null;
        }

        return memberRepository.findById(currentUserId)
                .orElseThrow(() -> new MemberException(MemberExceptionInfo.NOT_FOUND_MEMBER, currentUserId + "번 유저를 찾지 못했습니다."));
    }
}
