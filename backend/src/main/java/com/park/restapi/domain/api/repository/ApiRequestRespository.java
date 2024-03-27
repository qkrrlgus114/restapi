package com.park.restapi.domain.api.repository;

import com.park.restapi.domain.api.entity.ApiRequestRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiRequestRespository extends JpaRepository<ApiRequestRecord, Long> {
}
