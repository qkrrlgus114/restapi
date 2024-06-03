package com.park.restapi.domain.coupon.service;

import com.park.restapi.domain.coupon.dto.request.UpdateCouponQuantityRequestDTO;
import com.park.restapi.domain.coupon.dto.request.UpdateCouponSettingRequestDTO;
import com.park.restapi.domain.coupon.dto.response.CouponSettingResponseDTO;

public interface CouponService {
	// 쿠폰 획득
	void acquisitionCoupon();

	// 쿠폰 조회
	int getCoupons();

	// 쿠폰 상태 가져오기(관리자)
	CouponSettingResponseDTO getCouponSetting();

	// 쿠폰 상태 변경하기(관리자)
	void updateCouponSetting(UpdateCouponSettingRequestDTO requestDTO);

	// 쿠폰 개수 변경하기(관리자)
	int updateCouponQuantity(UpdateCouponQuantityRequestDTO requestDTO);
}
