package com.park.restapi.domain.coupon.controller;

import com.park.restapi.domain.coupon.dto.request.UpdateCouponQuantityRequestDTO;
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
@RequestMapping("/api/admin/coupons")
public class CouponAdminController {

    private final CouponService couponService;

    // 쿠폰 설정 상태 가져오기(관리자)
    @GetMapping("/settings")
    public ResponseEntity<ApiResponse<CouponSettingResponseDTO>> getCouponSetting() {
        CouponSettingResponseDTO couponSetting = couponService.getCouponSetting();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccess(couponSetting, "쿠폰 설정 데이터 조회 성공"));
    }

    // 쿠폰 설정 상태 변경하기(관리자)
    @PatchMapping("/settings")
    public ResponseEntity<ApiResponse<Void>> updateCouponSetting(@Valid @RequestBody UpdateCouponSettingRequestDTO updateCouponSettingRequestDTO) {
        couponService.updateCouponSetting(updateCouponSettingRequestDTO);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 쿠폰 즉시 발급하기(관리자)
    @PostMapping()
    public ResponseEntity<ApiResponse<Integer>> updateCouponQuantity(@Valid @RequestBody UpdateCouponQuantityRequestDTO updateCouponQuantityRequestDTO) {
        int result = couponService.updateCouponQuantity(updateCouponQuantityRequestDTO);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccess(result, "쿠폰이 발급되었습니다."));
    }
}
