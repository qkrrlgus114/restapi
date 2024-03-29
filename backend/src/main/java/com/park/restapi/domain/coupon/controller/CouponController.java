package com.park.restapi.domain.coupon.controller;

import com.park.restapi.domain.coupon.service.impl.CouponHistoryServiceImpl;
import com.park.restapi.domain.coupon.service.impl.CouponServiceImpl;
import com.park.restapi.util.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class CouponController {

    private final CouponHistoryServiceImpl couponHistoryService;
    private final CouponServiceImpl couponService;

    // 쿠폰 가져오기
    @PostMapping("coupons")
    public ResponseEntity<ApiResponse<?>> acquisitionCoupon(){
        couponService.acquisitionCoupon();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccessNoContent("쿠폰 획득 성공"));
    }
}
