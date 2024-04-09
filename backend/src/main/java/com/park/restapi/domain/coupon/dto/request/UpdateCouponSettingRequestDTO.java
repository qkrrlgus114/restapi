package com.park.restapi.domain.coupon.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateCouponSettingRequestDTO {
    @NotNull(message = "쿠폰 발급 여부를 선택해주세요.")
    private Boolean isDailyCouponGenerate;
    @NotNull(message = "쿠폰 발급 갯수를 입력해주세요.")
    @Min(value = 1, message = "쿠폰 발급 갯수는 1개 이상으로 입력해주세요.")
    @Max(value = 100, message = "쿠폰 발급 갯수는 100개 이하로 입력해주세요.")
    private Integer dailyCouponQuantity;
}
