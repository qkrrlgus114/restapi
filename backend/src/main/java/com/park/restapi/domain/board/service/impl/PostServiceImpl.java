package com.park.restapi.domain.board.service.impl;

import com.park.restapi.domain.board.dto.request.ApiRecommendPostRequestDTO;
import com.park.restapi.domain.board.dto.response.ApiRecommendPostResponseDTO;
import com.park.restapi.domain.board.dto.response.ApiRecommendPostsListResponseDTO;
import com.park.restapi.domain.board.dto.response.ApiRecommendPostsResponseDTO;
import com.park.restapi.domain.board.dto.response.TargetPostInfo;
import com.park.restapi.domain.board.entity.BoardType;
import com.park.restapi.domain.board.entity.Post;
import com.park.restapi.domain.board.entity.PostLike;
import com.park.restapi.domain.board.repository.PostLikeRepository;
import com.park.restapi.domain.board.repository.PostRepository;
import com.park.restapi.domain.board.service.PostService;
import com.park.restapi.domain.exception.exception.PostException;
import com.park.restapi.domain.exception.info.PostExceptionInfo;
import com.park.restapi.domain.member.entity.Member;
import com.park.restapi.util.entity.SearchType;
import com.park.restapi.util.entity.SortBy;
import com.park.restapi.util.service.MemberFindService;
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
    private final MemberFindService memberFindService;

    private final static int DEFAULT_DATA_COUNT = 10;

    // api 응답 공유게시판 등록
    @Override
    @Transactional
    public void apiRecommendDataPost(ApiRecommendPostRequestDTO apiRecommendPostRequestDTO) {
        Member currentMember = memberFindService.getCurrentMemberNull();

        // 게시글 생성
        Post post = apiRecommendPostRequestDTO.toEntity(currentMember, BoardType.SHARE, apiRecommendPostRequestDTO.methodType());
        postRepository.save(post);
    }

    // api 공유게시글 가져오기(페이지네이션)
    @Override
    @Transactional(readOnly = true)
    public ApiRecommendPostsListResponseDTO getGptApiRecommendPosts(int page, SearchType searchType, String searchKey, SortBy sortBy) {
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
        Member currentMember = memberFindService.getCurrentMemberNull();

        TargetPostInfo targetPostInfo = postRepository.findByIdWriteLockFetchJoinMemberDTO(postId)
                .orElseThrow(() -> new PostException(PostExceptionInfo.NOT_FOUND_POST, postId + "번 게시글이 존재하지 않습니다."));

        postRepository.findByIdUpdateViewCount(postId);

        // 좋아요 여부 찾기
        boolean isLiked = false;
        if (currentMember != null) {
            Optional<PostLike> byMemberAndPost = postLikeRepository.findByMemberAndPostId(currentMember, postId);
            if (byMemberAndPost.isPresent()) isLiked = true;
        }

        return ApiRecommendPostResponseDTO.builder()
                .postId(targetPostInfo.getId()).nickname(targetPostInfo.getNickname()).title(targetPostInfo.getTitle()).content(targetPostInfo.getContent())
                .createdDate(targetPostInfo.getCreatedDate()).likeCount(targetPostInfo.getLikeCount()).viewCount(targetPostInfo.getViewCount()).isLiked(isLiked).build();
    }
}
