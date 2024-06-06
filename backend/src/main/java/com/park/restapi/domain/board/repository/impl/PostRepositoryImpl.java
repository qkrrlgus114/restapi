package com.park.restapi.domain.board.repository.impl;

import com.park.restapi.domain.api.entity.MethodType;
import com.park.restapi.domain.board.dto.response.ApiRecommendPostsResponseDTO;
import com.park.restapi.domain.board.repository.PostCustomRepository;
import com.querydsl.core.types.OrderSpecifier;
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
    public Page<ApiRecommendPostsResponseDTO> findRecommendPosts(Pageable pageable, String searchType, String searchKey, String sortBy) {

        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sortBy);

        List<Long> postIds = queryFactory
                .select(post.id)
                .from(post)
                .where(searchCondition(searchType, searchKey))
                .orderBy(orderSpecifier, post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<ApiRecommendPostsResponseDTO> apiRecommendPostsResponseDTOS = queryFactory
                .select(Projections.constructor(ApiRecommendPostsResponseDTO.class,
                        post.id, post.methodType, post.title, member.nickname, post.likeCount, post.viewCount))
                .from(post)
                .leftJoin(post.member, member)
                .where(post.id.in(postIds))
                .fetch();

        long total = queryFactory
                .select(post.count())
                .from(post)
                .where(searchCondition(searchType, searchKey))
                .fetchOne();

        return new PageImpl<>(apiRecommendPostsResponseDTOS, pageable, total);
    }

    // 동적 검색 쿼리 생성
    private BooleanExpression searchCondition(String searchType, String searchKey) {
        if ("title".equals(searchType)) {
            return titleContains(searchKey);
        } else if ("nickname".equals(searchType)) {
            return nicknameContains(searchKey);
        } else if ("methodType".equals(searchType)) {
            return methodTypeContains(searchKey);
        }
        return null;
    }

    // 정렬 쿼리 생성
    private OrderSpecifier<?> getOrderSpecifier(String orderBy) {
        if ("like".equals(orderBy)) {
            return post.likeCount.desc();
        } else {
            return post.id.desc();
        }
    }

    // 제목 검색
    private BooleanExpression titleContains(String keyword) {
        return post.title.contains(keyword);
    }

    // 닉네임 검색
    private BooleanExpression nicknameContains(String keyword) {
        return post.member.nickname.contains(keyword);
    }

    // 메서드 검색
    private BooleanExpression methodTypeContains(String keyword) {
        return post.methodType.eq(MethodType.valueOf(keyword));
    }
}
