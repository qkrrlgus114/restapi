package com.park.restapi.domain.coupon.controller;

import com.park.restapi.domain.coupon.service.CouponService;
import com.park.restapi.util.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/coupons")
public class CouponController {

    private final CouponService couponService;

    // 유저가 쿠폰을 획득하는 API
    @PostMapping()
    public ResponseEntity<ApiResponse<Void>> acquisitionCoupons() {
        couponService.acquisitionCoupon();

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 현재 남은 쿠폰을 제공하는 API
    @GetMapping()
    public ResponseEntity<ApiResponse<Integer>> getCoupons() {
        int coupons = couponService.getCoupons();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccess(coupons, "남은 쿠폰의 개수 조회 성공"));
    }

}
