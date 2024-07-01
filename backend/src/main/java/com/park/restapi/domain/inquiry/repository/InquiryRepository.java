package com.park.restapi.domain.inquiry.repository;

import com.park.restapi.domain.inquiry.entity.Inquiry;
import com.park.restapi.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long>, InquiryCustomRepository {

    List<Inquiry> findByMember(Member member);

    @Query("select i from Inquiry i join fetch i.member left join fetch i.answer where i.id = :id")
    Optional<Inquiry> findByInquiryFetchJoinMember(@Param("id") Long id);
}
