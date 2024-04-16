package com.park.restapi.domain.refreshtoken.repository;

import com.park.restapi.domain.refreshtoken.entity.RefreshToken;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RefreshTokenCustomRepository {

    RefreshToken validatedRefreshToken(String accessTokenValue, String refreshTokenValue, LocalDateTime currentDate);
}
