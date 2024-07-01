package com.park.restapi.domain.refreshtoken.repository;

import com.park.restapi.domain.member.entity.Member;
import com.park.restapi.domain.refreshtoken.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>, RefreshTokenCustomRepository {
    RefreshToken findByMember(Member member);

}
