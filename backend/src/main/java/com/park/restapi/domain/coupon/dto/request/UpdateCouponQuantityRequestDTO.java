package com.park.restapi.domain.coupon.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record UpdateCouponQuantityRequestDTO(
        @Min(value = 0, message = "쿠폰 발급 갯수는 0개 이상으로 입력해주세요.")
        @Max(value = 100, message = "쿠폰 발급 갯수는 100개 이하로 입력해주세요.")
        int dailyCouponQuantity) {
}
