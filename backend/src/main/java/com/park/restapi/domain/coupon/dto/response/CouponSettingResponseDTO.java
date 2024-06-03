package com.park.restapi.domain.coupon.dto.response;

import com.park.restapi.domain.coupon.entity.CouponSetting;
import lombok.Builder;

@Builder
public record CouponSettingResponseDTO(
        Boolean isDailyCouponGenerate,
        Integer dailyCouponQuantity
) {
    public static CouponSettingResponseDTO toDTO(CouponSetting couponSetting) {
        return CouponSettingResponseDTO.builder()
                .isDailyCouponGenerate(couponSetting.getIsDailyCouponGenerate())
                .dailyCouponQuantity(couponSetting.getDailyCouponQuantity()).build();
    }
}
