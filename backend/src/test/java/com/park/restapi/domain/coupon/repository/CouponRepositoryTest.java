package com.park.restapi.domain.coupon.repository;

import com.park.restapi.domain.config.TestQuerydslConfiguration;
import com.park.restapi.domain.coupon.entity.Coupon;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@Import(TestQuerydslConfiguration.class)
class CouponRepositoryTest {

    @Autowired
    private CouponRepository couponRepository;

    Coupon savedCoupon;

    @BeforeEach
    void setup(){
        Coupon coupon = Coupon.builder()
                .remainingQuantity(5)
                .totalQuantity(5).build();

        savedCoupon = couponRepository.save(coupon);
    }

    @Test
    @DisplayName("쿠폰 조회(쓰기용 쿼리)")
    void findByCouponForWrite() {
        // given
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);

        // when
        Optional<Coupon> byCouponForWrite = couponRepository.findByCouponForWrite(startOfDay, endOfDay);

        // then
        Assertions.assertTrue(byCouponForWrite.isPresent());
        Coupon coupon = byCouponForWrite.get();
        Assertions.assertEquals(savedCoupon, coupon);
    }

    @Test
    @DisplayName("쿠폰 조회(읽기용 쿼리)")
    void findCouponForRead() {
        // given
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);

        // when
        Optional<Coupon> byCouponForWrite = couponRepository.findCouponForRead(startOfDay, endOfDay);

        // then
        Assertions.assertTrue(byCouponForWrite.isPresent());
        Coupon coupon = byCouponForWrite.get();
        Assertions.assertEquals(savedCoupon, coupon);
    }
}