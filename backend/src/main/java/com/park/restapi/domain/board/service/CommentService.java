package com.park.restapi.domain.board.service;

import com.park.restapi.domain.board.dto.request.CommentRequestDTO;
import com.park.restapi.domain.board.dto.response.CommentResponseDTO;

public interface CommentService {

    CommentResponseDTO addComment(Long postId, CommentRequestDTO commentRequestDTO);
}
