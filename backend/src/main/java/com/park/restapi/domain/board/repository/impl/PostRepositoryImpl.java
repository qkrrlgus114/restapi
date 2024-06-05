package com.park.restapi.domain.board.repository.impl;

import com.park.restapi.domain.board.dto.response.ApiRecommendPostsResponseDTO;
import com.park.restapi.domain.board.repository.PostCustomRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.park.restapi.domain.board.entity.QPost.post;
import static com.park.restapi.domain.member.entity.QMember.member;

@RequiredArgsConstructor
@Repository
@Slf4j
public class PostRepositoryImpl implements PostCustomRepository {

    private final JPAQueryFactory queryFactory;

    // 첫 페이지부터 데이터 가져오기
    @Override
    public Page<ApiRecommendPostsResponseDTO> findRecommendPostFirstPage(Pageable pageable) {

        List<ApiRecommendPostsResponseDTO> apiRecommendPostsResponseDTOS = queryFactory.
                select(Projections.constructor(ApiRecommendPostsResponseDTO.class,
                        post.id, post.methodType, post.title, member.nickname, post.likeCount, post.viewCount))
                .from(post)
                .leftJoin(post.member, member)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.id.desc())
                .fetch();

        long total = queryFactory
                .select(post.count())
                .from(post)
                .fetchOne();

        return new PageImpl<>(apiRecommendPostsResponseDTOS, pageable, total);
    }

    private BooleanExpression ltPostId(Long cursorId) {
        return cursorId == 0 ? null : post.id.lt(cursorId);
    }
}
