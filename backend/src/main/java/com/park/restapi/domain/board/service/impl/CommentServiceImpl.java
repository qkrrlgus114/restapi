package com.park.restapi.domain.board.service.impl;

import com.park.restapi.domain.board.dto.request.CommentRequestDTO;
import com.park.restapi.domain.board.dto.response.CommentResponseDTO;
import com.park.restapi.domain.board.entity.Comment;
import com.park.restapi.domain.board.entity.Post;
import com.park.restapi.domain.board.repository.CommentRepository;
import com.park.restapi.domain.board.repository.PostRepository;
import com.park.restapi.domain.board.service.CommentService;
import com.park.restapi.domain.exception.exception.MemberException;
import com.park.restapi.domain.exception.exception.PostException;
import com.park.restapi.domain.exception.info.MemberExceptionInfo;
import com.park.restapi.domain.exception.info.PostExceptionInfo;
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
public class CommentServiceImpl implements CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    // 댓글 작성
    @Override
    @Transactional
    public CommentResponseDTO addComment(Long postId, CommentRequestDTO commentRequestDTO) {
        Member currentMember = getCurrentMember();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostExceptionInfo.NOT_FOUND_POST, postId + "번 게시글을 찾을 수 없습니다."));

        // 댓글 저장
        Comment comment = commentRequestDTO.toEntity(post, currentMember);
        commentRepository.save(comment);

        return CommentResponseDTO.builder().comment(comment.getContent()).email(currentMember.getEmail()).createDate(comment.getCreatedDate()).build();
    }

    // 현재 로그인 유저 찾기
    private Member getCurrentMember() {
        Long currentUserId = jwtService.getCurrentUserId();
        return memberRepository.findById(currentUserId)
                .orElseThrow(() -> new MemberException(MemberExceptionInfo.NOT_FOUND_MEMBER, currentUserId + "번 유저를 찾지 못했습니다."));
    }
}
