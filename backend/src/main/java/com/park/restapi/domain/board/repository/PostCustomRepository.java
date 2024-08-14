package com.park.restapi.domain.board.repository;

import com.park.restapi.domain.board.dto.response.ApiRecommendPostsResponseDTO;
import com.park.restapi.util.entity.SearchType;
import com.park.restapi.util.entity.SortBy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostCustomRepository {

    Page<ApiRecommendPostsResponseDTO> findRecommendPosts(Pageable pageable, SearchType searchType, String searchKey, SortBy sortBy);
}
