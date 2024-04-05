package com.park.restapi.domain.coupon.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateCouponSettingRequestDTO {
    private Boolean isDailyCouponGenerate;
    private Integer dailyCouponQuantity;
}
