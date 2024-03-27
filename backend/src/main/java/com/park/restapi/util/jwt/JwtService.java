package com.park.restapi.util.jwt;

import com.park.restapi.domain.auth.entity.RefreshToken;
import com.park.restapi.domain.auth.repository.RefreshTokenRepository;
import com.park.restapi.domain.exception.exception.UserException;
import com.park.restapi.domain.exception.info.UserExceptionInfo;
import com.park.restapi.domain.user.entity.User;
import com.park.restapi.domain.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
    private final UserRepository userRepository;

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

        throw new UserException(UserExceptionInfo.NOT_FOUND_USER, "인증 정보가 잘못되었습니다.");
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
    * check = false -> 유저 서비스에서 요청
    * */
    public String createRefreshToken(Long userId, boolean check){
        Claims claims = Jwts.claims();
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty() && check) return "유저 없음";
        else if(userOptional.isEmpty() && !check){
            throw new UserException(UserExceptionInfo.NOT_FOUND_USER, "유저 데이터 없음");
        }

        User user = userOptional.get();

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenTime))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        RefreshToken refreshTokenData = refreshTokenRepository.findByUser(user);
        refreshTokenData.addTokenValueAndExpireDate(refreshToken, LocalDateTime.now().plusDays(14));

        return refreshToken;
    }

//    // 리프레시 토큰 확인
    public String checkRefreshToken(String refreshTokenValue) {
        Optional<RefreshToken> byId = refreshTokenRepository.findByValueAndExpireDateGreaterThan(refreshTokenValue, LocalDateTime.now());
        if(byId.isEmpty()) return "사용 불가";
        return "사용 가능";
    }

}
