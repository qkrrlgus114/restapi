package com.park.restapi.domain.member.service.impl;

import com.park.restapi.domain.auth.entity.RefreshToken;
import com.park.restapi.domain.auth.repository.RefreshTokenRepository;
import com.park.restapi.domain.coupon.entity.Coupon;
import com.park.restapi.domain.coupon.repository.CouponRepository;
import com.park.restapi.domain.exception.exception.CouponException;
import com.park.restapi.domain.exception.exception.MemberException;
import com.park.restapi.domain.exception.info.CouponExceptionInfo;
import com.park.restapi.domain.exception.info.MemberExceptionInfo;
import com.park.restapi.domain.member.dto.request.LoginInfoRequestDTO;
import com.park.restapi.domain.member.dto.request.SignUpRequstDTO;
import com.park.restapi.domain.member.dto.response.MemberInfoResponseDTO;
import com.park.restapi.domain.member.entity.Role;
import com.park.restapi.domain.member.entity.Member;
import com.park.restapi.domain.member.entity.MemberRole;
import com.park.restapi.domain.member.repository.MemberRepository;
import com.park.restapi.domain.member.repository.MemberRoleRepository;
import com.park.restapi.domain.member.service.MemberService;
import com.park.restapi.util.jwt.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder encoder;
    private final CouponRepository couponRepository;

    // 회원가입
    @Override
    @Transactional
    public void signUp(SignUpRequstDTO dto) throws IOException, InterruptedException {
        // 기존에 데이터가 있는지 확인
        if(memberRepository.existsByEmail(dto.getEmail())){
            throw new MemberException(MemberExceptionInfo.EXIST_USER_SIGNUP_DATA, "회원가입 중복 데이터 발생");
        }

        // 유저 생성
        Member member = Member.builder()
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .loginLastDate(LocalDateTime.now()).build();

        Member saveMember = memberRepository.save(member);

        // 유저 역할 생성
        MemberRole memberRole = MemberRole.builder()
                .member(saveMember)
                .role(Role.USER).build();
        memberRoleRepository.save(memberRole);

        // 리프레시 토큰 데이터 생성
        RefreshToken refreshToken = RefreshToken.builder()
                .member(member)
                .expireDate(null)
                .value(null).build();
        refreshTokenRepository.save(refreshToken);
    }

    // 이메일 중복 확인
    @Override
    public boolean existEmailCheck(String email) {
        return memberRepository.existsByEmail(email);
    }

    // 로그인
    @Override
    @Transactional
    public void login(LoginInfoRequestDTO dto, HttpServletResponse response) {
        Member member = memberRepository.findByMemberLogin(dto.getEmail())
                .orElseThrow(() -> new MemberException(MemberExceptionInfo.FAIL_LOGIN, "로그인 실패"));
        if(!encoder.matches(dto.getPassword(), member.getPassword())){
            throw new MemberException(MemberExceptionInfo.FAIL_LOGIN, "로그인 실패");
        }
        member.updateLoginDate();

        String accessToken = jwtService.createAccessToken(member.getId());
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");

        String refreshToken = jwtService.createRefreshToken(member.getId(), false);
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }

    // 소셜로그인
    @Override
    @Transactional
    public void socialLogin(HttpServletResponse response) {
        Long currentUserId = JwtService.getCurrentUserId();
        Member member = memberRepository.findById(currentUserId)
                .orElseThrow(() -> new MemberException(MemberExceptionInfo.FAIL_LOGIN, "로그인 실패"));
        member.updateLoginDate();

        String accessToken = jwtService.createAccessToken(member.getId());
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");

        String refreshToken = jwtService.createRefreshToken(member.getId(), false);
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }

    // 유저 정보 조회
    @Override
    public MemberInfoResponseDTO getUserInfo() {
        Long currentUserId = JwtService.getCurrentUserId();
        Member member = memberRepository.findById(currentUserId)
                .orElseThrow(() -> new MemberException(MemberExceptionInfo.NOT_FOUND_USER, "유저 데이터 없음"));

        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);

        log.info("시작 시간 {}", startOfDay);
        log.info("끝 시간 {}", endOfDay);
        List<Coupon> all = couponRepository.findAll();
        for(Coupon c : all){
            System.out.println(c.toString());
        }
        // 테스트

        Coupon coupon = couponRepository.findCouponForRead(startOfDay, endOfDay)
                .orElseThrow(() -> new CouponException(CouponExceptionInfo.FAIL_COUPON_DATA, "DB에 쿠폰 데이터 존재하지 않음."));

        return MemberInfoResponseDTO.fromEntity(member, coupon);
    }

    // 토큰 조회
    @Override
    public int getToken() {
        Long currentUserId = JwtService.getCurrentUserId();
        Member member = memberRepository.findById(currentUserId)
                .orElseThrow(() -> new MemberException(MemberExceptionInfo.NOT_FOUND_USER, "유저 데이터 없음"));

        return member.getToken();
    }

    // 로그아웃
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
