package com.park.restapi.domain.coupon.repository;

import com.park.restapi.domain.coupon.entity.CouponHistory;
import com.park.restapi.domain.member.entity.Member;
import com.park.restapi.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
class CouponHistoryRepositoryTest {

    @Autowired
    private CouponHistoryRepository couponHistoryRepository;
    @Autowired
    private MemberRepository memberRepository;

    CouponHistory savedCouponHistory;
    Member savedMember;

    @BeforeEach
    void setUp(){
        Member member = Member.builder()
                .email("test@naver.com")
                .nickname("test")
                .password("test")
                .loginLastDate(LocalDateTime.now()).build();
        savedMember = memberRepository.save(member);

        CouponHistory couponHistory = CouponHistory.builder()
                .member(savedMember).build();
        savedCouponHistory = couponHistoryRepository.save(couponHistory);
    }

    @Test
    @DisplayName("오늘 쿠폰 획득 기록 있음")
    void findByCouponHistory(){
        // given
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);

        // when
        CouponHistory searchCouponHistory = couponHistoryRepository.findByCouponHistory(startOfDay, endOfDay, savedMember);

        // then
        Assertions.assertEquals(savedCouponHistory, searchCouponHistory);
        Assertions.assertEquals(savedMember, searchCouponHistory.getMember());
    }

    @Test
    @DisplayName("오늘 쿠폰 획득 기록 없음")
    void findByCouponHistoryNo(){

    }
}