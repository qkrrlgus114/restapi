package com.park.restapi.domain.api.repository;

import com.park.restapi.domain.api.entity.ApiRequestHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiRequestHistoryRepository extends JpaRepository<ApiRequestHistory, Long>, ApiRequestHistoryCustomRepository {

    @EntityGraph(attributePaths = {"member"})
    Slice<ApiRequestHistory> findSliceBy(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"member"})
    Page<ApiRequestHistory> findAll(Pageable pageable);

}
