package com.park.restapi.domain.api.repository;

import com.park.restapi.domain.api.entity.ApiRequestHistory;
import com.park.restapi.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApiRequestHistoryRepository
        extends JpaRepository<ApiRequestHistory, Long>, ApiRequestHistoryCustomRepository {

    @EntityGraph(attributePaths = {"member"})
    Slice<ApiRequestHistory> findSliceBy(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"member"})
    Page<ApiRequestHistory> findAll(Pageable pageable);

    // 유저가 여태 사용했던 모든 토큰의 개수
    @Query("select count(api.member) from ApiRequestHistory api where api.member = :member")
    int findByTotalUseToken(@Param("member") Member member);
  
}
