package com.park.restapi.domain.member.repository;

import com.park.restapi.domain.member.entity.EmailConfirm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailConfirmRepository extends JpaRepository<EmailConfirm, Long> {

    // 인증번호 일치 확인
    @Query("select e from EmailConfirm e where e.certificationNumber = :code and e.certificationStatus = false")
    Optional<EmailConfirm> checkCode(@Param("code") String code);
}
