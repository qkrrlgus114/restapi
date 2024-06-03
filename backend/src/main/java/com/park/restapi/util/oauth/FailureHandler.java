package com.park.restapi.util.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public final class FailureHandler implements AuthenticationFailureHandler {

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        log.error("oauth 실패 핸들러 : " + exception.getMessage());

        // url 생성
        String url = makeRedirectUrl();

        redirectStrategy.sendRedirect(request, response, url);
    }

    // 리다이렉트 주소
//    private String makeRedirectUrl() {
//        return UriComponentsBuilder.fromUriString("https://restapi.store/failure")
//                .encode(StandardCharsets.UTF_8)
//                .build().toUriString();
//    }

    // 리다이렉트 주소
    private String makeRedirectUrl() {
        return UriComponentsBuilder.fromUriString("http://localhost:5173/failure")
                .encode(StandardCharsets.UTF_8)
                .build().toUriString();
    }
}