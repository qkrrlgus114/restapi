package com.park.restapi.domain.inquiry.repository;

import com.park.restapi.domain.inquiry.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

}
