package com.park.restapi.domain.api.repository.impl;

import com.park.restapi.domain.api.dto.response.RequestHistoryResponseDTO;
import com.park.restapi.domain.api.entity.ApiRequestHistory;
import com.park.restapi.domain.api.repository.ApiRequestHistoryRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

import static com.park.restapi.domain.api.entity.QApiRequestHistory.apiRequestHistory;
import static com.park.restapi.domain.member.entity.QMember.member;

@RequiredArgsConstructor
public class ApiRequestHistoryRepositoryRepositoryImpl implements ApiRequestHistoryRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<RequestHistoryResponseDTO> searchApiRequestHistory(Pageable pageable) {
        // 요청 이력 조회 쿼리 생성
        List<ApiRequestHistory> results = queryFactory
                .selectFrom(apiRequestHistory)
                .leftJoin(apiRequestHistory.member, member).fetchJoin()
                .orderBy(apiRequestHistory.requestDate.desc())
                .offset(pageable.getOffset()) 
                .limit(pageable.getPageSize()) 
                .fetch();

        // 결과를 DTO로 변환
        List<RequestHistoryResponseDTO> dtoList = results.stream()
                .map(entity ->
                        RequestHistoryResponseDTO.builder()
                                .memberId(entity.getMember().getId())
                                .requestDate(entity.getRequestDate())
                                .email(entity.getMember().getEmail())
                                .methodType(entity.getMethodType())
                                .requestContent(entity.getRequestContent())
                                .responseContent(entity.getResponseContent())
                                .build())
                .collect(Collectors.toList());

        // 전체 요청 이력 수 조회를 위한 쿼리 생성
        long total = queryFactory
                .select(apiRequestHistory.count())
                .from(apiRequestHistory)
                .fetchOne();

        // Page 객체 생성 및 반환
        return new PageImpl<>(dtoList, pageable, total);
    }

    // 검색 조건에 따른 요청 이력 조회
    @Override
    public Page<RequestHistoryResponseDTO> searchApiRequestHistoryByCondition(Pageable pageable, String searchType, String keyword) {

        BooleanExpression searchCondition = searchCondition(searchType, keyword);

        // 요청 이력 조회 쿼리 생성
        List<ApiRequestHistory> results = queryFactory
                .selectFrom(apiRequestHistory)
                .where(searchCondition)
                .leftJoin(apiRequestHistory.member, member).fetchJoin()
                .orderBy(apiRequestHistory.requestDate.desc()) // 요청 날짜 기준 내림차순 정렬
                .offset(pageable.getOffset()) // 페이지네이션 시작점
                .limit(pageable.getPageSize()) // 페이지 크기
                .fetch();// 쿼리 실행 및 결과 fetch

        // 결과를 DTO로 변환
        List<RequestHistoryResponseDTO> dtoList = results.stream()
                .map(entity ->
                        RequestHistoryResponseDTO.builder()
                                .memberId(entity.getMember().getId())
                                .requestDate(entity.getRequestDate())
                                .email(entity.getMember().getEmail())
                                .methodType(entity.getMethodType())
                                .requestContent(entity.getRequestContent())
                                .responseContent(entity.getResponseContent())
                                .build())
                .collect(Collectors.toList());

        // 전체 요청 이력 수 조회를 위한 쿼리 생성
        long total = queryFactory
                .select(apiRequestHistory.count())
                .from(apiRequestHistory)
                .where(searchCondition)
                .fetchOne();

        return new PageImpl<>(dtoList, pageable, total);
    }

    // 이메일 검색
    private BooleanExpression emailContains(String keyword) {
        return apiRequestHistory.member.email.contains(keyword);
    }
    // ID 검색
    private BooleanExpression idContains(Long memberId) {
        return apiRequestHistory.member.id.eq(memberId);
    }

    // 검색 조건 쿼리 생성
    private BooleanExpression searchCondition(String searchType, String keyword) {
        if("email".equals(searchType)) {
            return emailContains(keyword);
        }else if("id".equals(searchType)){
            return idContains(Long.parseLong(keyword));
        }
        return null;
    }

}
