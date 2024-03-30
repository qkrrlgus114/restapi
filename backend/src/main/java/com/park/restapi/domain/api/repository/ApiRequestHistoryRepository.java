package com.park.restapi.domain.api.repository;

import com.park.restapi.domain.api.entity.ApiRequestHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiRequestHistoryRepository extends JpaRepository<ApiRequestHistory, Long> {
}
