package com.park.restapi.domain.coupon.controller;

import com.park.restapi.domain.coupon.dto.request.UpdateCouponSettingRequestDTO;
import com.park.restapi.domain.coupon.dto.response.CouponSettingResponseDTO;
import com.park.restapi.domain.coupon.service.CouponService;
import com.park.restapi.util.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class CouponController {

    private final CouponService couponService;

    // 유저가 쿠폰을 획득하는 API
    @PostMapping("coupons")
    public ResponseEntity<ApiResponse<?>> acquisitionCoupons(){
        couponService.acquisitionCoupon();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccessNoContent("토큰을 획득하셨습니다."));
    }

    // 현재 남은 쿠폰을 제공하는 API
    @GetMapping("coupons")
    public ResponseEntity<ApiResponse<?>> getCoupons(){
        int coupons = couponService.getCoupons();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccess(coupons, "남은 쿠폰의 개수 조회 성공"));
    }

    // 쿠폰 설정 상태 가져오기(관리자)
    @GetMapping("admin/coupons/setting")
    public ResponseEntity<ApiResponse<?>> getCouponSetting(){
        CouponSettingResponseDTO couponSetting = couponService.getCouponSetting();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccess(couponSetting, "쿠폰 설정 데이터 조회 성공"));
    }

    // 쿠폰 설정 상태 변경하기(관리자)
    @PatchMapping("admin/coupons/setting")
    public ResponseEntity<ApiResponse<?>> updateCouponSetting(@Valid @RequestBody UpdateCouponSettingRequestDTO requestDTO){
        couponService.updateCouponSetting(requestDTO);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccessNoContent("쿠폰 설정이 변경되었습니다."));
    }
}
