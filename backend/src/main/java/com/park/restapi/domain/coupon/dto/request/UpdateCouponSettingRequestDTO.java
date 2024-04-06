package com.park.restapi.domain.coupon.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateCouponSettingRequestDTO {
    @NotBlank(message = "쿠폰 발급 여부를 선택해주세요.")
    private Boolean isDailyCouponGenerate;
    @NotBlank(message = "쿠폰 발급 갯수를 입력해주세요.")
    @Size(min = 1, max = 100, message = "쿠폰 발급 갯수는 1개 이상 100개 이하로 입력해주세요.")
    private Integer dailyCouponQuantity;
}
