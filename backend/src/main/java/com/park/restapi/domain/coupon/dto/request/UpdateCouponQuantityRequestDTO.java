package com.park.restapi.domain.coupon.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateCouponQuantityRequestDTO {

	@NotNull(message = "쿠폰 발급 갯수를 입력해주세요.")
	@Min(value = 0, message = "쿠폰 발급 갯수는 0개 이상으로 입력해주세요.")
	@Max(value = 100, message = "쿠폰 발급 갯수는 100개 이하로 입력해주세요.")
	private int dailyCouponQuantity;

	public UpdateCouponQuantityRequestDTO(@NotNull(message = "쿠폰 발급 갯수를 입력해주세요.") int dailyCouponQuantity) {
		this.dailyCouponQuantity = dailyCouponQuantity;
	}
}
