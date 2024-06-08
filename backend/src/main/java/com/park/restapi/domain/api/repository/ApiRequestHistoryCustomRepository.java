package com.park.restapi.domain.api.repository;

import com.park.restapi.domain.api.dto.response.ApiRequestHistoryResponseDTO;
import com.park.restapi.util.entity.SearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApiRequestHistoryCustomRepository {
    Page<ApiRequestHistoryResponseDTO> searchApiRequestHistory(Pageable pageable);

    Page<ApiRequestHistoryResponseDTO> searchApiRequestHistoryByCondition(Pageable pageable, SearchType searchType,
                                                                          String keyword);
}
