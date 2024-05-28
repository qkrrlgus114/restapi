package com.park.restapi.domain.inquiry.repository;

import com.park.restapi.domain.inquiry.entity.Inquiry;
import com.park.restapi.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    List<Inquiry> findByMember(Member member);
}
