package com.park.restapi.util.oauth.handler;

import com.park.restapi.domain.member.entity.Member;
import com.park.restapi.domain.member.repository.MemberRepository;
import com.park.restapi.util.jwt.JwtService;
import com.park.restapi.util.oauth.RegistrationId;
import com.park.restapi.util.oauth.userinfo.OAuth2UserInfo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class SuccessHandler implements AuthenticationSuccessHandler {

    private static final String LOCAL_URL = "http://localhost:5173/success";
    private static final String SERVER_URL = "https://restapi.store/success";
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";

    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    // oauth 성공 핸들러
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("oauth 성공 핸들러 동작");

        Member member = findMember(authentication);

        // 액세스 토큰 생성
        String accessToken = jwtService.createAccessToken(member.getId());
        // 리프레시 토큰 생성
        String refreshToken = jwtService.createRefreshToken(member.getId(), true, accessToken);

        tokenSavedInCookie(accessToken, refreshToken, response);
        // url 생성
        String url = makeRedirectUrl();

        redirectStrategy.sendRedirect(request, response, url);
    }

    private void tokenSavedInCookie(String accessToken, String refreshToken, HttpServletResponse response) {
        Cookie accessTokenCookie = new Cookie(ACCESS_TOKEN, accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");

        Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN, refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");

        response.addCookie(refreshTokenCookie);
        response.addCookie(accessTokenCookie);
    }

    private Member findMember(Authentication authentication) {
        OAuth2User principal = (OAuth2User) authentication.getPrincipal();

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        String registrationId = token.getAuthorizedClientRegistrationId().toUpperCase();
        RegistrationId regId = RegistrationId.valueOf(registrationId);
        OAuth2UserInfo oAuth2UserInfo = regId.getOAuth2UserInfo(principal.getAttributes(), regId);

        String email = oAuth2UserInfo.getEmail();

        // 유저 없으면 예외처리
        return memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
    }

    // 리다이렉트 주소
    private String makeRedirectUrl() {
        return UriComponentsBuilder.fromUriString(SERVER_URL)
//        return UriComponentsBuilder.fromUriString(LOCAL_URL)
                .encode(StandardCharsets.UTF_8)
                .build().toUriString();
    }

}