package com.park.restapi.domain.refreshtoken.service;

import jakarta.servlet.http.HttpServletResponse;

public interface RefreshTokenService {
	void reGenerateToken(HttpServletResponse response, String accessToken, String refreshToken);
}
