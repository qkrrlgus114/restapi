package com.park.restapi.domain.member.repository;

import com.park.restapi.domain.member.dto.response.MemberInfoResponseDTO;
import com.park.restapi.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("select m from Member m join fetch m.memberRoles where m.id = :id")
    Optional<Member> findByIdFetchRole(@Param("id") Long id);

    // 로그인
    @Query("select m from Member m where m.email = :email")
    Optional<Member> findByMemberLogin(@Param("email") String email);

    // 소셜 로그인 회원가입
    @Query("select m from Member m join fetch m.memberRoles where m.id = :id")
    Optional<Member> findByIdLogin(@Param("id") Long id);

    // 탈퇴한지 30일 지난 유저 탐색
    @Query("select m from Member m where m.withdrawalDate is not null and  m.withdrawalDate <= :thirtyDaysAgo")
    List<Member> findByWithdrawalMember(@Param("thirtyDaysAgo") LocalDateTime thirtyDaysAgo);
}
