package com.park.restapi.util.oauth;

import com.park.restapi.domain.member.entity.Member;
import com.park.restapi.domain.member.repository.MemberRepository;
import com.park.restapi.util.jwt.JwtService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;


/*
 * 여기서 처리해야 되는 내용
 * 1. 액세스 토큰 생성
 * 2. 리프레시 토큰 생성
 * 3. 쿠키에 리프레시 담음.
 * 4. 클라이언트에게 필요한 dto 전달
 * */
@Slf4j
@Component
@RequiredArgsConstructor
public class SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("oauth 성공 핸들러 동작");
        OAuth2User principal = (OAuth2User) authentication.getPrincipal();

        // user의 이메일 추출
        Map<String, Object> attributes = principal.getAttributes();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        String email = String.valueOf(kakaoAccount.get("email"));

        // 유저 없으면 예외처리
        Optional<Member> byEmail = Optional.ofNullable(memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new));

        Member member = byEmail.get();

        // 액세스 토큰 생성
        String accessToken = jwtService.createAccessToken(member.getId());

        // 리프레시 토큰 생성
        String refreshToken = jwtService.createRefreshToken(member.getId(), true);
        System.out.println(accessToken);
        System.out.println(refreshToken);

        // 액세스 토큰을 위한 쿠키 생성(세션쿠키)
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true); // HTTPS를 사용하는 경우에만 true로 설정
        accessTokenCookie.setPath("/");

        // 리프레시 토큰을 위한 쿠키 생성(세션쿠키)
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true); // HTTPS를 사용하는 경우에만 true로 설정
        refreshTokenCookie.setPath("/");

        // 쿠키에 토큰 저장
        response.addCookie(refreshTokenCookie);
        response.addCookie(accessTokenCookie);

        // url 생성
        String url = makeRedirectUrl();

        redirectStrategy.sendRedirect(request, response, url);

    }

    // 리다이렉트 주소
//    private String makeRedirectUrl() {
//        return UriComponentsBuilder.fromUriString("https://restapi.store/success")
//                .encode(StandardCharsets.UTF_8)
//                .build().toUriString();
//    }

    // 리다이렉트 주소
    private String makeRedirectUrl() {
        return UriComponentsBuilder.fromUriString("http://localhost:5173/success")
                .encode(StandardCharsets.UTF_8)
                .build().toUriString();
    }

}