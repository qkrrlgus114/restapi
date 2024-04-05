package com.park.restapi.domain.coupon.dto.response;

import com.park.restapi.domain.coupon.entity.CouponSetting;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponSettingResponseDTO {
    private Boolean isDailyCouponGenerate;
    private Integer dailyCouponQuantity;

    @Builder
    public CouponSettingResponseDTO(Boolean isDailyCouponGenerate, Integer dailyCouponQuantity) {
        this.isDailyCouponGenerate = isDailyCouponGenerate;
        this.dailyCouponQuantity = dailyCouponQuantity;
    }

    public static CouponSettingResponseDTO fromEntity(CouponSetting couponSetting){
        return CouponSettingResponseDTO.builder()
                .dailyCouponQuantity(couponSetting.getDailyCouponQuantity())
                .isDailyCouponGenerate(couponSetting.getIsDailyCouponGenerate()).build();
    }
}
