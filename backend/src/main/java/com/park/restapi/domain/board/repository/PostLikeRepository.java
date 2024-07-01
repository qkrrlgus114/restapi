package com.park.restapi.domain.board.repository;

import com.park.restapi.domain.board.entity.Post;
import com.park.restapi.domain.board.entity.PostLike;
import com.park.restapi.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    @Query("select p from PostLike p where p.member = :member and p.post.id = :id")
    Optional<PostLike> findByMemberAndPostId(@Param("member") Member member, @Param("id") Long postId);

    Optional<PostLike> findByMemberAndPost(Member member, Post post);

    boolean existsByMemberAndPost(Member member, Post post);
}
