package com.park.restapi.domain.refreshtoken.controller;

import com.park.restapi.domain.refreshtoken.service.impl.RefreshTokenServiceImpl;
import com.park.restapi.util.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/auth")
@Validated
public class RefreshTokenController {
    private final RefreshTokenServiceImpl refreshTokenService;

    @PostMapping("refresh-token")
    public ResponseEntity<ApiResponse<?>> refreshToken(@CookieValue(value = "accessToken", re) String accessToken,
                                                       @CookieValue(value = "refreshToken") String refreshToken,
                                                       HttpServletResponse response) {
        refreshTokenService.reGenerateToken(response, accessToken, refreshToken);
        return ResponseEntity.ok(ApiResponse.createSuccessNoContent("토큰 재발급 완료"));
    }
}
