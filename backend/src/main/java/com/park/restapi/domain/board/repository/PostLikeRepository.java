package com.park.restapi.domain.board.repository;

import com.park.restapi.domain.board.entity.Post;
import com.park.restapi.domain.board.entity.PostLike;
import com.park.restapi.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByMemberAndPost(Member member, Post post);

    boolean existsByMemberAndPost(Member member, Post post);
}
