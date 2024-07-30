package com.park.restapi.domain.member.repository;

import com.park.restapi.domain.member.entity.Member;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
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

    // 탈퇴한지 30일 지난 유저 탐색
    @Query("select m from Member m where m.withdrawalDate is not null and  m.withdrawalDate <= :thirtyDaysAgo")
    List<Member> findByWithdrawalMember(@Param("thirtyDaysAgo") LocalDateTime thirtyDaysAgo);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select m from Member m where m.id = :id")
    Optional<Member> findByIdLock(@Param("id") Long id);

    // 토큰 개수 반환
    @Query("select m.token from Member m where m.id = :id")
    Optional<Integer> findByMemberToken(@Param("id") Long id);
}
