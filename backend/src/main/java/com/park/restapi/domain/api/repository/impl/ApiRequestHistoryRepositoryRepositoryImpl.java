package com.park.restapi.domain.api.repository.impl;

import com.park.restapi.domain.api.dto.response.RequestHistoryResponseDTO;
import com.park.restapi.domain.api.entity.ApiRequestHistory;
import com.park.restapi.domain.api.entity.QApiRequestHistory;
import com.park.restapi.domain.api.repository.ApiRequestHistoryRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ApiRequestHistoryRepositoryRepositoryImpl implements ApiRequestHistoryRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<RequestHistoryResponseDTO> searchApiRequestHistory(Pageable pageable) {
        QApiRequestHistory apiRequestHistory = QApiRequestHistory.apiRequestHistory;
        // 요청 이력 조회 쿼리 생성
        List<ApiRequestHistory> results = queryFactory
                .selectFrom(apiRequestHistory)
                .orderBy(apiRequestHistory.requestDate.desc()) // 요청 날짜 기준 내림차순 정렬
                .offset(pageable.getOffset()) // 페이지네이션 시작점
                .limit(pageable.getPageSize()) // 페이지 크기
                .fetch(); // 쿼리 실행 및 결과 fetch

        // 결과를 DTO로 변환
        List<RequestHistoryResponseDTO> dtoList = results.stream()
                .map(entity -> new RequestHistoryResponseDTO(
                        // entity to DTO 변환 로직
                        entity.getId(),
                        entity.getMember().getId(),
                        entity.getRequestDate(),
                        entity.getMethodType(),
                        entity.getRequestContent(),
                        entity.getResponseContent()
                ))
                .collect(Collectors.toList());

        // 전체 요청 이력 수 조회를 위한 쿼리 생성
        long total = queryFactory
                .select(apiRequestHistory.count())
                .from(apiRequestHistory)
                .fetchOne();

        // Page 객체 생성 및 반환
        return new PageImpl<>(dtoList, pageable, total);
    }
}
