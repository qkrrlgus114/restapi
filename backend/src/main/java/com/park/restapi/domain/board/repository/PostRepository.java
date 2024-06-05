package com.park.restapi.domain.board.repository;

import com.park.restapi.domain.board.entity.Post;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Post p join fetch p.member where p.id = :id")
    Optional<Post> findByIdWriteLockFetchJoinMember(@Param("id") Long id);

}
