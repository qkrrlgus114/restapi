package com.park.restapi.domain.member.service.impl;

import com.park.restapi.domain.coupon.repository.CouponRepository;
import com.park.restapi.domain.exception.exception.MemberException;
import com.park.restapi.domain.member.dto.request.DeactivateRequestDTO;
import com.park.restapi.domain.member.dto.request.LoginInfoRequestDTO;
import com.park.restapi.domain.member.dto.request.SignUpRequestDTO;
import com.park.restapi.domain.member.dto.response.MemberInfoResponseDTO;
import com.park.restapi.domain.member.entity.Member;
import com.park.restapi.domain.member.entity.MemberRole;
import com.park.restapi.domain.member.entity.Role;
import com.park.restapi.domain.member.entity.SocialType;
import com.park.restapi.domain.member.repository.MemberRepository;
import com.park.restapi.domain.member.repository.MemberRoleRepository;
import com.park.restapi.util.jwt.JwtService;
import com.park.restapi.util.service.MemberFindService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private MemberRoleRepository memberRoleRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private BCryptPasswordEncoder encoder;
    @Mock
    private CouponRepository couponRepository;
    @Mock
    private HttpServletResponse httpServletResponse;
    @Mock
    private MemberFindService memberFindService;
    @InjectMocks
    private MemberServiceImpl memberService;
    @Mock
    private Member mockingMember = mock(Member.class);

    private Member mockMember;
    private SignUpRequestDTO signUpRequestDTO;

    @BeforeEach
    void setUp() {
        signUpRequestDTO = SignUpRequestDTO.builder()
                .email("test@naver.com")
                .password("1234")
                .nickname("테스트").build();
        mockMember = Member.builder()
                .email(signUpRequestDTO.email())
                .password("encoded_password")
                .nickname(signUpRequestDTO.nickname())
                .socialType(SocialType.GENERAL)
                .loginLastDate(LocalDateTime.now()).build();

    }

    @Test
    @DisplayName("회원가입에 성공한다.")
    void successSignup() throws IOException, InterruptedException {
        // given
        MemberRole mockMemberRole = MemberRole.builder()
                .member(mockMember).build();

        when(memberRepository.existsByEmail(any())).thenReturn(false);
        when(memberRepository.save(any(Member.class))).thenReturn(mockMember);
        when(encoder.encode(signUpRequestDTO.password())).thenReturn("encoded_password");

        // when
        memberService.signUp(signUpRequestDTO);

        // then
        verify(memberRepository).existsByEmail(signUpRequestDTO.email());
        verify(encoder).encode(signUpRequestDTO.password());
    }

    @Test
    @DisplayName("회원가입 중복 시도")
    void alreadyJoin() {
        // given
        String email = "test@naver.com";
        when(memberRepository.existsByEmail(email)).thenReturn(true);

        // then
        MemberException memberException = assertThrows(MemberException.class, () -> {
            memberService.signUp(signUpRequestDTO);
        });

        assertEquals("M-001", memberException.getException().getCode());
    }

    @Test
    @DisplayName("이메일 중복확인 true 반환")
    void checkEmailTrue() {
        // given
        String email = "test@naver.com";

        // when
        when(memberRepository.existsByEmail(email)).thenReturn(true);

        // then
        assertTrue(memberService.existEmailCheck(email));
        verify(memberRepository).existsByEmail(email);
    }

    @Test
    @DisplayName("이메일 중복확인 false 반환")
    void checkEmailFalse() {
        // given
        String email = "test@naver.com";

        // when
        when(memberRepository.existsByEmail(email)).thenReturn(false);

        // then
        assertFalse(memberService.existEmailCheck(email));
        verify(memberRepository).existsByEmail(email);
    }

    @Test
    @DisplayName("로그인 실패(이메일 없음)")
    void tryLoginNotMatchEmail() {
        // given
        String email = "testt@naver.com";
        LoginInfoRequestDTO loginInfoRequestDTO = new LoginInfoRequestDTO(email, "test", false);
        when(memberRepository.findByMemberLogin(email)).thenReturn(Optional.empty());

        // then
        MemberException memberException = assertThrows(MemberException.class, () -> {
            memberService.login(loginInfoRequestDTO, httpServletResponse);
        });

        assertEquals("M-002", memberException.getException().getCode());
        assertEquals(HttpStatus.UNAUTHORIZED, memberException.getException().getStatus());
        verify(memberRepository).findByMemberLogin(email);
    }

    @Test
    @DisplayName("로그인 실패(추방된 유저)")
    void tryLoginKickedEmail() {
        // given
        String email = "testt@naver.com";
        Member kickedMockMember = Member.builder()
                .email(email)
                .password("encoded_password")
                .nickname(signUpRequestDTO.nickname())
                .loginLastDate(LocalDateTime.now()).build();
        kickedMockMember.updateBannedDate();

        LoginInfoRequestDTO loginInfoRequestDTO = new LoginInfoRequestDTO(email, "test", false);
        when(memberRepository.findByMemberLogin(email)).thenReturn(Optional.of(kickedMockMember));

        // then
        MemberException memberException = assertThrows(MemberException.class, () -> {
            memberService.login(loginInfoRequestDTO, httpServletResponse);
        });

        assertEquals("M-007", memberException.getException().getCode());
        assertEquals(HttpStatus.BAD_REQUEST, memberException.getException().getStatus());
        verify(memberRepository).findByMemberLogin(email);
    }

    @Test
    @DisplayName("로그인 실패(탈퇴한 유저)")
    void tryLoginWithdrawalEmail() {
        // given
        String email = "testt@naver.com";
        Member withdrawalMockMember = Member.builder()
                .email(email)
                .password("encoded_password")
                .nickname(signUpRequestDTO.nickname())
                .loginLastDate(LocalDateTime.now()).build();
        withdrawalMockMember.updateWithdrawalDate();

        LoginInfoRequestDTO loginInfoRequestDTO = new LoginInfoRequestDTO(email, "test", false);
        when(memberRepository.findByMemberLogin(email)).thenReturn(Optional.of(withdrawalMockMember));

        // then
        MemberException memberException = assertThrows(MemberException.class, () -> {
            memberService.login(loginInfoRequestDTO, httpServletResponse);
        });

        assertEquals("M-008", memberException.getException().getCode());
        assertEquals(HttpStatus.BAD_REQUEST, memberException.getException().getStatus());
        verify(memberRepository).findByMemberLogin(email);
    }


    @Test
    @DisplayName("소셜 로그인 성공")
    void successSocialLogin() {

        String mockAccessToken = "mockAccessToken";
        String mockRefreshToken = "mockRefreshToken";


        when(memberFindService.getCurrentMember()).thenReturn(mockingMember);
        when(jwtService.createAccessToken(mockingMember.getId())).thenReturn(mockAccessToken);
        when(jwtService.createRefreshToken(mockingMember.getId(), false, mockAccessToken)).thenReturn(mockRefreshToken);

        memberService.socialLogin(httpServletResponse);

        verify(memberFindService).getCurrentMember();
        verify(mockingMember).updateLoginDate();
        verify(jwtService).createAccessToken(mockingMember.getId());
        verify(jwtService).createRefreshToken(mockingMember.getId(), false, mockAccessToken);

        verify(httpServletResponse).addCookie(argThat(new CookieMatcher("accessToken", mockAccessToken)));
        verify(httpServletResponse).addCookie(argThat(new CookieMatcher("refreshToken", mockRefreshToken)));
    }

    @Test
    @DisplayName("유저 정보 조회 성공")
    void findUserInformation() {
        when(memberFindService.getCurrentMemberFetchJoinRoles()).thenReturn(mockMember);
        MemberInfoResponseDTO memberInfoResponseDTO = MemberInfoResponseDTO.toDTO(mockMember);

        MemberInfoResponseDTO userInfo = memberService.getUserInfo();

        verify(memberFindService).getCurrentMemberFetchJoinRoles();
        assertEquals(memberInfoResponseDTO, userInfo);
    }

    @Test
    @DisplayName("유저 탈퇴 성공")
    void successWithdrawalMember() {
        // given
        DeactivateRequestDTO deactivateRequestDTO = new DeactivateRequestDTO(SocialType.GENERAL, "1234");
        when(memberFindService.getCurrentMemberFetchJoinRoles()).thenReturn(mockingMember);
        when(encoder.matches(deactivateRequestDTO.password(), mockingMember.getPassword())).thenReturn(true);

        // when
        memberService.deactivateGeneralMember(deactivateRequestDTO);

        // then
        verify(memberFindService).getCurrentMemberFetchJoinRoles();
        verify(encoder).matches(deactivateRequestDTO.password(), mockingMember.getPassword());
        verify(mockingMember).updateWithdrawalDate();
    }

    @Test
    @DisplayName("유저 탈퇴 실패(비밀번호 불일치)")
    void failWithdrawalMemberNotMatchPassword() {
        // given
        DeactivateRequestDTO deactivateRequestDTO = new DeactivateRequestDTO(SocialType.GENERAL, "4321");
        when(memberFindService.getCurrentMemberFetchJoinRoles()).thenReturn(mockingMember);
        when(encoder.matches(deactivateRequestDTO.password(), mockingMember.getPassword())).thenReturn(false);

        // when
        MemberException memberException = assertThrows(MemberException.class, () -> {
            memberService.deactivateGeneralMember(deactivateRequestDTO);
        });
        assertEquals("M-009", memberException.getException().getCode());
        verify(memberFindService).getCurrentMemberFetchJoinRoles();
        verify(encoder).matches(deactivateRequestDTO.password(), mockingMember.getPassword());
    }

    @Test
    @DisplayName("유저 탈퇴 실패(관리자 계정)")
    void failWithdrawalMemberIsAdmin() {
        // given
        DeactivateRequestDTO deactivateRequestDTO = new DeactivateRequestDTO(SocialType.GENERAL, "1234");
        when(memberFindService.getCurrentMemberFetchJoinRoles()).thenReturn(mockingMember);
        when(encoder.matches(deactivateRequestDTO.password(), mockingMember.getPassword())).thenReturn(true);
        when(mockingMember.getMemberRoles()).thenReturn(Collections.singletonList(new MemberRole(mockingMember, Role.ADMIN)));

        // when
        MemberException memberException = assertThrows(MemberException.class, () -> {
            memberService.deactivateGeneralMember(deactivateRequestDTO);
        });

        assertEquals("M-010", memberException.getException().getCode());
        verify(memberFindService).getCurrentMemberFetchJoinRoles();
        verify(encoder).matches(deactivateRequestDTO.password(), mockingMember.getPassword());
        verify(mockingMember, never()).updateWithdrawalDate();
    }

    @Test
    @DisplayName("유저 추방 성공")
    void successKickedMember() {
        // given
        when(memberFindService.getCurrentMember()).thenReturn(mockingMember);
        Long id = 1L;

        // when
        memberService.bannedMember(id);

        // then
        verify(mockingMember).updateBannedDate();
    }

    @Test
    @DisplayName("유저 토큰 초기화 진행 성공(스케줄러)")
    void successTokenReset() {
        // given
        Member member = Member.builder()
                .email("test2@naver.com")
                .password("1234")
                .nickname("asdasd")
                .socialType(SocialType.GENERAL)
                .loginLastDate(LocalDateTime.now()).build();
        member.useToken();
        mockMember.useToken();
        List<Member> list = List.of(member, mockMember);

        when(memberRepository.findAll()).thenReturn(list);

        // when
        memberService.resetAllTokens();

        // then
        for (int i = 0; i < list.size(); i++) {
            assertEquals(10, list.get(i).getToken());
        }
        verify(memberRepository).findAll();
    }

    private static class CookieMatcher implements ArgumentMatcher<Cookie> {
        private final String name;
        private final String value;

        public CookieMatcher(String name, String value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public boolean matches(Cookie cookie) {
            return name.equals(cookie.getName()) && value.equals(cookie.getValue());
        }
    }

}