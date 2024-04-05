package com.park.restapi.util.jwt;

import com.park.restapi.domain.member.entity.Member;
import com.park.restapi.domain.member.entity.MemberRole;
import com.park.restapi.domain.member.repository.MemberRepository;
import com.park.restapi.domain.member.repository.MemberRoleRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Value("${jwt.secretkey}")
    private String SECRET_KEY;
    @Value("#{${jwt.access-validity}}")
    private Long accessTokenTime;
    @Value("#{${jwt.refresh-validity}}")
    private Long refreshTokenTime;
    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;

    @Override // 이 주소로 오는 건 토큰 없어도 됨.
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        return path.startsWith("/api/authentications/send") || path.startsWith("/api/email-check") || path.startsWith("/api/phonenumber-check") || path.startsWith("/login")
                || path.startsWith("/api/authentications/verify") || path.startsWith("/api/signup") || path.startsWith("/api/login") || path.startsWith("/oauth2/authorization/kakao");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("jwt 필터 동작");

        System.out.println(request.getRequestURI());
        // 헤더에서 액세스 토큰 추출
        Cookie[] cookies = request.getCookies();

        // 쿠키 자체가 없으면 401 에러 발생
        if (cookies == null) {
            log.error("쿠키 없음.");
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "쿠키가 존재하지 않습니다.");
            return;
        }

        // 액세스 토큰 추출
        String accessToken = Arrays.stream(cookies)
                .filter(c -> "accessToken".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        // 액세스 토큰이 없는 경우
        if (accessToken == null) {
            log.error("액세스 토큰이 없습니다.");
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "액세스 토큰이 존재하지 않습니다.");
            return; // 여기서 처리 종료
        }

        // userId 토큰에서 꺼냄.
        try {
            TokenInfo tokenInfo = jwtService.getUserId(accessToken);
            log.info("userId:{}", tokenInfo.getUserId());

            // 토큰이 만료됐으면
            if (tokenInfo.isExpired()) {
                // 리프레시 토큰 탐색
                String refreshToken = Arrays.stream(cookies)
                        .filter(c -> "refreshToken".equals(c.getName()))
                        .findFirst()
                        .map(Cookie::getValue)
                        .orElse(null);

                // 만약에 null이면
                if (refreshToken == null) {
                    log.error("리프레시 토큰이 없습니다.");
                    sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "리프레시 토큰이 존재하지 않습니다.");
                    return;
                }

                String result = jwtService.checkRefreshToken(refreshToken);

                if ("사용 불가".equals(result)) {
                    log.error("리프레시 토큰 문제 발생");
                    sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "리프레시 토큰 사용 불가(로그아웃 진행)");
                    return; // 여기서 처리 종료
                } else {
                    String reAccessToken = jwtService.createAccessToken(tokenInfo.getUserId());
                    String reRefreshToken = jwtService.createRefreshToken(tokenInfo.getUserId(), true);

                    if ("유저 없음".equals(reRefreshToken)) {
                        log.error("유저 없음");
                        sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "유저 정보 조회 실패");
                        return; // 여기서 처리 종료
                    }

                    // 액세스 토큰을 위한 쿠키 생성
                    Cookie accessTokenCookie = new Cookie("accessToken", reAccessToken);
                    accessTokenCookie.setHttpOnly(true);
                    accessTokenCookie.setSecure(true); // HTTPS를 사용하는 경우에만 true로 설정
                    accessTokenCookie.setPath("/");

                    // 리프레시 토큰을 위한 쿠키 생성
                    Cookie refreshTokenCookie = new Cookie("refreshToken", reRefreshToken);
                    refreshTokenCookie.setHttpOnly(true);
                    refreshTokenCookie.setSecure(true); // HTTPS를 사용하는 경우에만 true로 설정
                    refreshTokenCookie.setPath("/");

                    // 쿠키에 토큰 저장
                    response.addCookie(refreshTokenCookie);
                    response.addCookie(accessTokenCookie);

                    sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "JWT 재발급 완료");
                    return; // 여기서 처리 종료
                }
            }

            Optional<Member> userOptional = memberRepository.findById(tokenInfo.getUserId());
            if(userOptional.isEmpty()) {
                log.info("유저 데이터 없음");
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "유저 데이터 없음");
                return;
            }

            Member member = userOptional.get();

            List<MemberRole> memberRoles = memberRoleRepository.findByMember(member);

            List<GrantedAuthority> authorities = memberRoles.stream()
                    .map(memberRole -> new SimpleGrantedAuthority(memberRole.getRole().name()))
                    .collect(Collectors.toList());

            // 권한 부여
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(member.getId(), null, authorities);

            // Detail을 넣어준다.
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 공통 응답 메시지
    public static void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws
            IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(String.format("{\"error\": \"%s\"}", message));
    }
}
