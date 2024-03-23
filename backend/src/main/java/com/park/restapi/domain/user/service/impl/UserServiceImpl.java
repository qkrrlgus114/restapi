package com.park.restapi.domain.user.service.impl;

import com.park.restapi.domain.auth.entity.RefreshToken;
import com.park.restapi.domain.auth.repository.RefreshTokenRepository;
import com.park.restapi.domain.exception.exception.UserException;
import com.park.restapi.domain.exception.info.UserExceptionInfo;
import com.park.restapi.domain.user.dto.request.LoginInfoRequestDTO;
import com.park.restapi.domain.user.dto.request.SignUpRequstDTO;
import com.park.restapi.domain.user.dto.response.LoginInfoResponseDTO;
import com.park.restapi.domain.user.entity.Role;
import com.park.restapi.domain.user.entity.User;
import com.park.restapi.domain.user.entity.UserRole;
import com.park.restapi.domain.user.repository.UserRepository;
import com.park.restapi.domain.user.repository.UserRoleRepository;
import com.park.restapi.domain.user.service.UserService;
import com.park.restapi.util.jwt.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder encoder;

    // 회원가입
    @Override
    @Transactional
    public void signUp(SignUpRequstDTO dto) throws IOException{
        // 기존에 데이터가 있는지 확인
        if(userRepository.existsByEmail(dto.getEmail())){
            throw new UserException(UserExceptionInfo.EXIST_USER_SIGNUP_DATA, "회원가입 중복 데이터 발생");
        }

        // 유저 생성
        User user = User.builder()
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .loginLastDate(LocalDateTime.now()).build();

        User saveUser = userRepository.save(user);

        // 유저 역할 생성
        UserRole userRole = UserRole.builder()
                .user(saveUser)
                .role(Role.USER).build();
        userRoleRepository.save(userRole);

        // 리프레시 토큰 데이터 생성
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .expireDate(null)
                .value(null).build();
        refreshTokenRepository.save(refreshToken);

    }

    // 이메일 중복 확인
    @Override
    public boolean existEmailCheck(String email) {
        return userRepository.existsByEmail(email);
    }

    // 로그인
    @Override
    @Transactional
    public LoginInfoResponseDTO login(LoginInfoRequestDTO dto, HttpServletResponse response) {
        User user = userRepository.findByUserLogin(dto.getEmail())
                .orElseThrow(() -> new UserException(UserExceptionInfo.FAIL_LOGIN, "로그인 실패"));
        if(!encoder.matches(dto.getPassword(), user.getPassword())){
            throw new UserException(UserExceptionInfo.FAIL_LOGIN, "로그인 실패");
        }

        String accessToken = jwtService.createAccessToken(user.getId());
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");

        String refreshToken = jwtService.createRefreshToken(user.getId(), false);
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);


        return LoginInfoResponseDTO.fromEntity(user);
    }

    // 소셜로그인
    @Override
    public LoginInfoResponseDTO socialLogin(HttpServletResponse response) {
        Long currentUserId = JwtService.getCurrentUserId();
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserException(UserExceptionInfo.FAIL_LOGIN, "로그인 실패"));
        String accessToken = jwtService.createAccessToken(user.getId());
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");

        String refreshToken = jwtService.createRefreshToken(user.getId(), false);
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);


        return LoginInfoResponseDTO.fromEntity(user);
    }

    @Override
    public int getToken() {
        Long currentUserId = JwtService.getCurrentUserId();
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserException(UserExceptionInfo.NOT_FOUND_USER, "유저 데이터 없음"));

        return user.getToken();
    }

    @Override
    public void logout(HttpServletResponse response) {
        Cookie accessTokenCookie = new Cookie("accessToken", null);
        accessTokenCookie.setMaxAge(0);
        accessTokenCookie.setPath("/");
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);

        Cookie jsessionidCookie = new Cookie("JSESSIONID", null);
        jsessionidCookie.setMaxAge(0);
        jsessionidCookie.setPath("/");
        response.addCookie(jsessionidCookie);
    }


}
