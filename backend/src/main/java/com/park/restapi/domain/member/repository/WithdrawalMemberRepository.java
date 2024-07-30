package com.park.restapi.domain.member.repository;

import com.park.restapi.domain.member.entity.WithdrawalMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WithdrawalMemberRepository extends JpaRepository<WithdrawalMember, Long> {
    Optional<WithdrawalMember> findByEmail(String email);
}
