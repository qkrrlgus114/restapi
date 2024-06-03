package com.park.restapi.util.jwt;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenInfo {
	private Long userId;
	private boolean isExpired;

	@Builder
	public TokenInfo(Long userId, boolean isExpired) {
		this.userId = userId;
		this.isExpired = isExpired;
	}
}
