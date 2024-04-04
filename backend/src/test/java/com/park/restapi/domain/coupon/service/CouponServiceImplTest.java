package com.park.restapi.domain.coupon.service;

import com.park.restapi.domain.coupon.entity.Coupon;
import com.park.restapi.domain.coupon.repository.CouponHistoryRepository;
import com.park.restapi.domain.coupon.repository.CouponRepository;
import com.park.restapi.domain.coupon.service.impl.CouponServiceImpl;
import com.park.restapi.domain.exception.exception.CouponException;
import com.park.restapi.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CouponServiceImplTest {

    @Mock
    private CouponRepository couponRepository;
    @Mock
    private CouponHistoryRepository couponHistoryRepository;
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private CouponServiceImpl couponService;

    private Coupon coupon;

    @BeforeEach
    void setUp() {
        coupon = Coupon.builder()
                .totalQuantity(5)
                .remainingQuantity(5).build();
    }

    @Test
    void acquisitionCoupon() {
    }

    @Test
    void 남은_선착순_쿠폰_조회() {
        // given
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        Mockito.when(couponRepository.findCouponForRead(startOfDay, endOfDay)).thenReturn(Optional.of(coupon));

        // when
        int coupons = couponService.getCoupons();

        // then
        Assertions.assertEquals(5, coupons);
    }

    @Test
    void 남은_선착순_쿠폰_데이터_없음_예외발생(){
        // given
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        Mockito.when(couponRepository.findCouponForRead(startOfDay, endOfDay)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThrows(CouponException.class, () -> couponService.getCoupons());
    }
}