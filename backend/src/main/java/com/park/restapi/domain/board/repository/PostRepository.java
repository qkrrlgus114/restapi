package com.park.restapi.domain.board.repository;

import com.park.restapi.domain.board.dto.response.TargetPostInfo;
import com.park.restapi.domain.board.entity.Post;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select new com.park.restapi.domain.board.dto.response.TargetPostInfo(" +
            "p.id, m.nickname, p.title, p.content, p.createdDate, p.likeCount, p.viewCount) " +
            "from Post p left join p.member m where p.id = :id")
    Optional<TargetPostInfo> findByIdWriteLockFetchJoinMemberDTO(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("update Post p set p.viewCount = p.viewCount + 1 where p.id = :id")
    void findByIdUpdateViewCount(@Param("id") Long id);

    @Query("select p from Post p join fetch p.member m where p.id = :id")
    Optional<Post> findByIdWriteLockFetchJoinMember(@Param("id") Long id);
}
