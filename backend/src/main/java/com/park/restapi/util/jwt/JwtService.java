package com.park.restapi.util.jwt;

import com.park.restapi.domain.exception.exception.MemberException;
import com.park.restapi.domain.exception.info.MemberExceptionInfo;
import com.park.restapi.domain.member.entity.Member;
import com.park.restapi.domain.member.repository.MemberRepository;
import com.park.restapi.domain.refreshtoken.entity.RefreshToken;
import com.park.restapi.domain.refreshtoken.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    @Value("${jwt.secretkey}")
    private String SECRET_KEY;
    @Value("#{${jwt.access-validity}}")
    private Long accessTokenTime;
    @Value("#{${jwt.refresh-validity}}")
    private Long refreshTokenTime;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    // 유저 pk 꺼내기
    public TokenInfo getUserId(String token){
        try {
            Long userId = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token)
                    .getBody().get("userId", Long.class);
            return TokenInfo.builder()
                    .userId(userId)
                    .isExpired(false).build();
        } catch (ExpiredJwtException e) {
            // 만료된 JWT에서도 userId는 가져옴.
            Long userId = e.getClaims().get("userId", Long.class);
            return TokenInfo.builder()
                    .userId(userId)
                    .isExpired(true).build();
        }
    }

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            return (Long) authentication.getPrincipal();
        }

        throw new MemberException(MemberExceptionInfo.NOT_FOUND_USER, "인증 정보가 잘못되었습니다.");
    }

    // 액세스 토큰 생성
    public String createAccessToken(Long userId){
        Claims claims = Jwts.claims();
        claims.put("userId", userId);

        return Jwts.builder() // 액세스 토큰을 생성
                .setClaims(claims) // 유저의 pk값
                .setIssuedAt(new Date(System.currentTimeMillis())) // 현재 시간
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenTime)) // 언제까지
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // 뭐로 사인됐는지
                .compact();
    }

    /*
    * 리프레시 토큰 생성
    * check = true -> 필터에서 요청
    * check = false -> 서비스에서 요청
    * */
    @Transactional
    public String createRefreshToken(Long userId, boolean check, String accessToken){
        Claims claims = Jwts.claims();
        Optional<Member> userOptional = memberRepository.findById(userId);
        if(userOptional.isEmpty() && check) return "유저 없음";
        else if(userOptional.isEmpty() && !check){
            throw new MemberException(MemberExceptionInfo.NOT_FOUND_USER, "유저 데이터 없음");
        }

        Member member = userOptional.get();

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenTime))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        RefreshToken entity = RefreshToken.toEntity(accessToken, refreshToken, member, LocalDateTime.now().plusDays(14));
        refreshTokenRepository.save(entity);

        return refreshToken;
    }

}
