package com.park.restapi.domain.refreshtoken.repository;

import com.park.restapi.domain.refreshtoken.entity.RefreshToken;

import java.time.LocalDateTime;

public interface RefreshTokenCustomRepository {

    RefreshToken validatedRefreshToken(String accessTokenValue, String refreshTokenValue, LocalDateTime currentDate);
}
