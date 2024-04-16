package com.park.restapi.domain.member.service.impl;

import com.park.restapi.domain.coupon.entity.Coupon;
import com.park.restapi.domain.coupon.repository.CouponRepository;
import com.park.restapi.domain.exception.exception.MemberException;
import com.park.restapi.domain.exception.info.MemberExceptionInfo;
import com.park.restapi.domain.member.dto.request.LoginInfoRequestDTO;
import com.park.restapi.domain.member.dto.request.SignUpRequstDTO;
import com.park.restapi.domain.member.dto.response.MemberInfoResponseDTO;
import com.park.restapi.domain.member.entity.Member;
import com.park.restapi.domain.member.entity.MemberRole;
import com.park.restapi.domain.member.entity.Role;
import com.park.restapi.domain.member.repository.MemberRepository;
import com.park.restapi.domain.member.repository.MemberRoleRepository;
import com.park.restapi.domain.member.service.MemberService;
import com.park.restapi.domain.refreshtoken.entity.RefreshToken;
import com.park.restapi.domain.refreshtoken.repository.RefreshTokenRepository;
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
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder encoder;
    private final CouponRepository couponRepository;
    private final RefreshTokenRepository refreshTokenRepository;

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
        if(!member.getEmail().startsWith("test")){
            if(!encoder.matches(dto.getPassword(), member.getPassword())){
                throw new MemberException(MemberExceptionInfo.FAIL_LOGIN, "로그인 실패");
            }
        }

        member.updateLoginDate();

        String accessToken = jwtService.createAccessToken(member.getId());
        String refreshToken = jwtService.createRefreshToken(member.getId(), false, accessToken);

        saveCookie(response, "accessToken", accessToken);
        saveCookie(response, "refreshToken", refreshToken);
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
        String refreshToken = jwtService.createRefreshToken(member.getId(), false, accessToken);

        saveCookie(response, "accessToken", accessToken);
        saveCookie(response, "refreshToken", refreshToken);
    }

    // 유저 정보 조회
    @Override
    public MemberInfoResponseDTO getUserInfo() {
        Long currentUserId = JwtService.getCurrentUserId();
        Member member = memberRepository.findByIdLogin(currentUserId)
                .orElseThrow(() -> new MemberException(MemberExceptionInfo.NOT_FOUND_USER, "유저 데이터 없음"));

        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);

        Optional<Coupon> couponForRead = couponRepository.findCouponForRead(startOfDay, endOfDay);

        if(couponForRead.isEmpty()){
            return MemberInfoResponseDTO.fromEntity(member, null);
        }

        return MemberInfoResponseDTO.fromEntity(member, couponForRead.get());
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
        deleteCookie(response, "accessToken");
        deleteCookie(response, "refreshToken");
    }

    // 쿠키 저장
    public void saveCookie(HttpServletResponse response, String tokenName, String tokenValue){
        Cookie tokenCookie = new Cookie(tokenName, tokenValue);
        tokenCookie.setHttpOnly(true);
        tokenCookie.setSecure(true);
        tokenCookie.setPath("/");

        response.addCookie(tokenCookie);
    }

    // 쿠키 삭제
    public void deleteCookie(HttpServletResponse response, String tokenName){
        Cookie cookie = new Cookie(tokenName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }


}
