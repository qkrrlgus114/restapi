package com.park.restapi.domain.api.repository.impl;

import com.park.restapi.domain.api.dto.response.ApiRequestHistoryResponseDTO;
import com.park.restapi.domain.api.repository.ApiRequestHistoryCustomRepository;
import com.park.restapi.util.entity.SearchType;
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

import static com.park.restapi.domain.api.entity.QApiRequestHistory.apiRequestHistory;
import static com.park.restapi.domain.member.entity.QMember.member;

@RequiredArgsConstructor
@Repository
@Slf4j
public class ApiRequestHistoryRepositoryImpl implements ApiRequestHistoryCustomRepository {

    private final JPAQueryFactory queryFactory;

    // 모든 api 요청 내역 조회
    @Override
    public Page<ApiRequestHistoryResponseDTO> searchApiRequestHistory(Pageable pageable) {

        List<ApiRequestHistoryResponseDTO> results = queryFactory.select(
                        Projections.constructor(ApiRequestHistoryResponseDTO.class, apiRequestHistory.member.id,
                                apiRequestHistory.requestDate, member.email, apiRequestHistory.methodType,
                                apiRequestHistory.requestContent, apiRequestHistory.responseContent))
                .from(apiRequestHistory)
                .leftJoin(apiRequestHistory.member, member)
                .orderBy(apiRequestHistory.requestDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(apiRequestHistory.count())
                .from(apiRequestHistory)
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }

    // 검색 조건에 따른 요청 이력 조회
    @Override
    public Page<ApiRequestHistoryResponseDTO> searchApiRequestHistoryByCondition(Pageable pageable, SearchType searchType,
                                                                                 String keyword) {
        BooleanExpression searchCondition = searchCondition(searchType, keyword);

        List<ApiRequestHistoryResponseDTO> results = queryFactory.select(
                        Projections.constructor(ApiRequestHistoryResponseDTO.class, apiRequestHistory.member.id,
                                apiRequestHistory.requestDate, member.email, apiRequestHistory.methodType,
                                apiRequestHistory.requestContent, apiRequestHistory.responseContent))
                .from(apiRequestHistory)
                .leftJoin(apiRequestHistory.member, member)
                .where(searchCondition)
                .orderBy(apiRequestHistory.requestDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(apiRequestHistory.count())
                .from(apiRequestHistory)
                .where(searchCondition)
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }

    // 이메일 검색
    private BooleanExpression emailContains(String keyword) {
        return apiRequestHistory.member.email.contains(keyword);
    }

    // 아이디 검색
    private BooleanExpression idEquals(String keyword) {
        return apiRequestHistory.member.id.eq(Long.parseLong(keyword));
    }

    // 검색 조건 쿼리 생성
    private BooleanExpression searchCondition(SearchType searchType, String keyword) {
        return switch (searchType) {
            case ID -> idEquals(keyword);
            case EMAIL -> emailContains(keyword);
            default -> null;
        };
    }

}
