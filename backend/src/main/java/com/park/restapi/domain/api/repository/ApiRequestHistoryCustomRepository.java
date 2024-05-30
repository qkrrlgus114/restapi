package com.park.restapi.domain.api.repository;

import com.park.restapi.domain.api.dto.response.ApiRequestHistoryResponseDTO;
import com.park.restapi.domain.api.entity.ApiRequestHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApiRequestHistoryCustomRepository {
    Page<ApiRequestHistory> searchApiRequestHistory(Pageable pageable);
    Page<ApiRequestHistoryResponseDTO> searchApiRequestHistoryByCondition(Pageable pageable, String searchType, String keyword);
}
