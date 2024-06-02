package com.park.restapi.domain.refreshtoken.service.impl;

import com.park.restapi.domain.exception.exception.MemberException;
import com.park.restapi.domain.exception.info.MemberExceptionInfo;
import com.park.restapi.domain.refreshtoken.entity.RefreshToken;
import com.park.restapi.domain.refreshtoken.repository.RefreshTokenRepository;
import com.park.restapi.domain.refreshtoken.service.RefreshTokenService;
import com.park.restapi.util.jwt.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public void reGenerateToken(HttpServletResponse response, String accessToken, String refreshToken) {
        log.info("리프레시 토큰 재발급 시작");
        // 1. 리프레시 토큰 살아있는지 검증
        RefreshToken refreshTokenData = refreshTokenRepository.validatedRefreshToken(accessToken ,refreshToken, LocalDateTime.now());
        if(refreshTokenData == null) throw new MemberException(MemberExceptionInfo.NOT_FOUND_REFRESH_TOKEN, "리프레시 토큰이 존재하지 않습니다.");

        // 2. 액세스 토큰 재발급
        String reAccessToken = jwtService.createAccessToken(refreshTokenData.getMember().getId());
        // 3. 리프레시 토큰 재발급
        String reRefreshToken = jwtService.createRefreshToken(refreshTokenData.getMember().getId(), false, reAccessToken);

        saveCookie(reAccessToken, response, "accessToken");
        saveCookie(reRefreshToken, response, "refreshToken");
    }

    // 쿠키 저장
    public void saveCookie(String token, HttpServletResponse response, String name){
        Cookie cookie = new Cookie(name, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // HTTPS를 사용하는 경우에만 true로 설정
        cookie.setPath("/");

        response.addCookie(cookie);
    }
}
