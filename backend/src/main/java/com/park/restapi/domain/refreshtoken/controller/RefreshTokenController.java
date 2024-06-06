package com.park.restapi.domain.refreshtoken.controller;

import com.park.restapi.domain.refreshtoken.service.RefreshTokenService;
import com.park.restapi.util.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
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
@RequestMapping("/api")
@Validated
public class RefreshTokenController {
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<Void>> refreshToken(@CookieValue(value = "accessToken") String accessToken,
                                                          @CookieValue(value = "refreshToken") String refreshToken,
                                                          HttpServletResponse response) {
        refreshTokenService.reGenerateToken(response, accessToken, refreshToken);
        return ResponseEntity.ok(ApiResponse.createSuccessNoContent("토큰 재발급 완료"));
    }
}
