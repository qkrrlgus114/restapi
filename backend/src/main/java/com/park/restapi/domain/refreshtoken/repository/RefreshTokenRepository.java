package com.park.restapi.domain.refreshtoken.repository;

import com.park.restapi.domain.refreshtoken.entity.RefreshToken;
import com.park.restapi.domain.member.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>, RefreshTokenCustomRepository {
    RefreshToken findByMember(Member member);

}
