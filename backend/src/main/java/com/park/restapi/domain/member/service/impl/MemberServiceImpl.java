package com.park.restapi.domain.member.service.impl;

import com.park.restapi.domain.api.repository.ApiRequestHistoryRepository;
import com.park.restapi.domain.coupon.repository.CouponHistoryRepository;
import com.park.restapi.domain.exception.exception.MemberException;
import com.park.restapi.domain.exception.info.MemberExceptionInfo;
import com.park.restapi.domain.member.dto.request.DeactivateRequestDTO;
import com.park.restapi.domain.member.dto.request.LoginInfoRequestDTO;
import com.park.restapi.domain.member.dto.request.SignUpRequestDTO;
import com.park.restapi.domain.member.dto.response.MemberInfoResponseDTO;
import com.park.restapi.domain.member.dto.response.MyInfoResponseDTO;
import com.park.restapi.domain.member.entity.*;
import com.park.restapi.domain.member.repository.MemberRepository;
import com.park.restapi.domain.member.repository.MemberRoleRepository;
import com.park.restapi.domain.member.repository.WithdrawalMemberRepository;
import com.park.restapi.domain.member.service.MemberService;
import com.park.restapi.util.jwt.JwtService;
import com.park.restapi.util.service.MemberFindService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final ApiRequestHistoryRepository apiRequestHistoryRepository;
    private final CouponHistoryRepository couponHistoryRepository;
    private final WithdrawalMemberRepository withdrawalMemberRepository;
    private final MemberFindService memberFindService;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder encoder;

    // 회원가입
    @Override
    @Transactional
    public void signUp(SignUpRequestDTO signUpRequestDTO) throws IOException, InterruptedException {
        // 기존에 데이터가 있는지 확인
        if (memberRepository.existsByEmail(signUpRequestDTO.email())) {
            throw new MemberException(MemberExceptionInfo.EXIST_MEMBER_SIGNUP_DATA, "회원가입 중복 데이터 발생");
        }

        // 유저 생성
        Member member = Member.builder()
                .nickname(signUpRequestDTO.nickname())
                .email(signUpRequestDTO.email())
                .password(encoder.encode(signUpRequestDTO.password()))
                .loginLastDate(LocalDateTime.now())
                .socialType(SocialType.GENERAL).build();
        Member saveMember = memberRepository.save(member);

        // 유저 역할 생성
        MemberRole memberRole = MemberRole.builder()
                .member(saveMember)
                .build();
        memberRoleRepository.save(memberRole);
    }

    // 이메일 중복 확인
    @Override
    @Transactional(readOnly = true)
    public boolean existEmailCheck(String email) {
        return memberRepository.existsByEmail(email);
    }

    // 로그인
    @Override
    @Transactional
    public void login(LoginInfoRequestDTO loginInfoRequestDTO, HttpServletResponse response) {
        Member member = memberRepository.findByMemberLogin(loginInfoRequestDTO.email())
                .orElseThrow(() -> new MemberException(MemberExceptionInfo.FAIL_LOGIN, loginInfoRequestDTO.email() + "에 맞는 유저를 찾지 못했습니다.(로그인 실패)"));

        // 추방 여부 판단
        if (member.getBannedDate() != null) {
            throw new MemberException(MemberExceptionInfo.BANNED_MEMBER, loginInfoRequestDTO.email() + " 유저가 로그인 시도를 진행했습니다.(추방된 유저)");
        }

        // 탈퇴 여부 판단
        if (member.getWithdrawalDate() != null) {
            throw new MemberException(MemberExceptionInfo.WITHDRAWAL_MEMBER,
                    loginInfoRequestDTO.email() + " 유저가 로그인 시도를 진행했습니다.(탈퇴한 유저)");
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
        Member currentMember = memberFindService.getCurrentMember();

        currentMember.updateLoginDate();

        String accessToken = jwtService.createAccessToken(currentMember.getId());
        String refreshToken = jwtService.createRefreshToken(currentMember.getId(), false, accessToken);
        saveCookie(response, "accessToken", accessToken);
        saveCookie(response, "refreshToken", refreshToken);
    }

    // 유저 정보 조회
    @Override
    @Transactional(readOnly = true)
    public MemberInfoResponseDTO getUserInfo() {
        Member currentMember = memberFindService.getCurrentMemberFetchJoinRoles();

        return MemberInfoResponseDTO.toDTO(currentMember);
    }

    // 토큰 조회
    @Override
    @Transactional(readOnly = true)
    public int getToken() {
        Long currentUserId = jwtService.getCurrentUserId();
        return memberRepository.findByMemberToken(currentUserId)
                .orElseThrow(() -> new MemberException(MemberExceptionInfo.NOT_FOUND_MEMBER, currentUserId + "번 유저를 찾지 못했습니다."));
    }

    // 로그아웃
    @Override
    public void logout(HttpServletResponse response) {
        deleteCookie(response, "accessToken");
        deleteCookie(response, "refreshToken");
    }

    // 일반 유저 탈퇴
    @Override
    @Transactional
    public void deactivateGeneralMember(DeactivateRequestDTO deactivateRequestDTO) {
        Member currentMember = memberFindService.getCurrentMember();

        if (!encoder.matches(deactivateRequestDTO.password(), currentMember.getPassword())) {
            throw new MemberException(MemberExceptionInfo.NOT_MATCH_PASSWORD, currentMember.getEmail() + " 유저 비밀번호 불일치 발생(회원 탈퇴)");
        }

        if (isAdmin(currentMember))
            throw new MemberException(MemberExceptionInfo.NOT_WITHDRAWAL_ADMIN, currentMember.getEmail() + " 관리자 계정 탈퇴 시도.");
        currentMember.updateWithdrawalDate();
    }

    // 소셜 유저 탈퇴
    @Override
    @Transactional
    public void deactivateSocialMember() {
        Member currentMember = memberFindService.getCurrentMember();

        if (isAdmin(currentMember))
            throw new MemberException(MemberExceptionInfo.NOT_WITHDRAWAL_ADMIN, currentMember.getEmail() + " 관리자 계정 탈퇴 시도.");

        currentMember.updateWithdrawalDate();
    }

    // 유저 추방
    @Override
    @Transactional
    public void bannedMember(Long id) {
        Member currentMember = memberFindService.getCurrentMember();

        // 추방 시간 추가
        currentMember.updateBannedDate();
    }

    // 유저 개인 정보 제공
    @Override
    @Transactional(readOnly = true)
    public MyInfoResponseDTO getMemberInfo() {
        Member currentMember = memberFindService.getCurrentMember();

        // 유저가 여태 사용했던 토큰 개수
        int totalUseToken = apiRequestHistoryRepository.findByTotalUseToken(currentMember);

        // 유저가 여태 획득했던 토큰 개수
        int totalAcquisitionToken = couponHistoryRepository.findByMemberTotalAcquisitionToken(currentMember);

        return MyInfoResponseDTO.toDTO(totalUseToken, totalAcquisitionToken);
    }

    // 유저 데일리 토큰 초기화(스케줄러)
    @Override
    @Transactional
    public void resetAllTokens() {
        List<Member> all = memberRepository.findAll();
        for (Member u : all) {
            u.resetToken();
        }
    }

    // 유저 탈퇴 판단(스케줄러)
    @Override
    @Transactional
    public void withdrawalMember() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        List<Member> byWithdrawalMember = memberRepository.findByWithdrawalMember(thirtyDaysAgo);

        for (Member m : byWithdrawalMember) {
            memberRepository.delete(m);
            withdrawalMemberRepository.save(WithdrawalMember.builder().email(m.getEmail()).build());
        }
    }

    // 쿠키 저장
    private void saveCookie(HttpServletResponse response, String tokenName, String tokenValue) {
        Cookie tokenCookie = new Cookie(tokenName, tokenValue);
        tokenCookie.setHttpOnly(true);
        tokenCookie.setSecure(true);
        tokenCookie.setPath("/");

        response.addCookie(tokenCookie);
    }

    // 쿠키 삭제
    private void deleteCookie(HttpServletResponse response, String tokenName) {
        Cookie cookie = new Cookie(tokenName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    // 관리자 권한 확인
    private boolean isAdmin(Member member) {
        return member.getMemberRoles().stream()
                .anyMatch(role -> role.getRole() == (Role.ADMIN));
    }

}
