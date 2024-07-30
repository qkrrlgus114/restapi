package com.park.restapi.domain.member.service.impl;

import com.park.restapi.domain.exception.exception.EmailException;
import com.park.restapi.domain.exception.exception.MemberException;
import com.park.restapi.domain.member.entity.EmailConfirm;
import com.park.restapi.domain.member.entity.Member;
import com.park.restapi.domain.member.entity.SocialType;
import com.park.restapi.domain.member.entity.WithdrawalMember;
import com.park.restapi.domain.member.repository.EmailConfirmRepository;
import com.park.restapi.domain.member.repository.MemberRepository;
import com.park.restapi.domain.member.repository.WithdrawalMemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private JavaMailSender javaMailSender;
    @Mock
    private EmailConfirmRepository emailConfirmRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private WithdrawalMemberRepository withdrawalMemberRepository;
    @InjectMocks
    private EmailServiceImpl emailService;

    EmailConfirm emailConfirm;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        emailConfirm = EmailConfirm.builder()
                .certificationNumber("123456")
                .certificationStatus(false).build();
        emailConfirm.setCreatedDate(LocalDateTime.now());
    }

    @Test
    @DisplayName("인증번호 생성 성공")
    void createKeySuccess() {
        // when
        String key = emailService.createKey();

        // then
        assertEquals(10, key.length());
        assertTrue(key.matches("[A-Za-z0-9!@#$%^&*()]+"));
    }

    @Test
    @DisplayName("인증번호 사용 성공")
    void successUsedCode() {
        // given
        String code = "123456";
        Mockito.when(emailConfirmRepository.checkCode(code)).thenReturn(Optional.of(emailConfirm));

        // when
        emailService.checkCertificationCode(code);

        // then
        assertTrue(emailConfirm.getCertificationStatus());
    }

    @Test
    @DisplayName("인증번호 불일치")
    void notMatchCode() {
        // given
        String code = "654321";
        Mockito.when(emailConfirmRepository.checkCode(code)).thenReturn(Optional.empty());

        // then
        EmailException emailException = assertThrows(EmailException.class, () -> {
            emailService.checkCertificationCode(code);
        });

        assertEquals("인증번호가 일치하지 않습니다", emailException.getException().getMessage());
        assertEquals("E-002", emailException.getException().getCode());
    }

    @Test
    @DisplayName("인증번호 만료")
    void expirationCode() throws NoSuchFieldException, IllegalAccessException {
        // given
        emailConfirm.setCreatedDate(LocalDateTime.now().minusMinutes(5));
        String code = "123456";
        Mockito.when(emailConfirmRepository.checkCode(code)).thenReturn(Optional.of(emailConfirm));

        // then
        EmailException emailException = assertThrows(EmailException.class, () -> {
            emailService.checkCertificationCode(code);
        });

        assertEquals("E-003", emailException.getException().getCode());
        assertEquals("인증번호가 만료되었습니다.", emailException.getException().getMessage());
    }

    @Test
    @DisplayName("탈퇴된 유저로 회원가입을 시도")
    void withdrawalMemberTryJoin() throws Exception {
        // given
        String email = "test@naver.com";
        WithdrawalMember withdrawalMember = WithdrawalMember.builder().email(email).build();
        Mockito.when(withdrawalMemberRepository.findByEmail(email)).thenReturn(Optional.of(withdrawalMember));

        // then
        MemberException memberException = assertThrows(MemberException.class, () -> {
            emailService.sendSimpleMessageRegist(email);
        });

        assertEquals("M-008", memberException.getException().getCode());
    }

    @Test
    @DisplayName("중복 가입 진행")
    void alreadyJoin() {
        // given
        String email = "test@naver.com";
        Member member = Member.builder()
                .email(email)
                .nickname("test")
                .password("test")
                .socialType(SocialType.GENERAL)
                .build();

        Mockito.when(withdrawalMemberRepository.findByEmail(email)).thenReturn(Optional.empty());
        Mockito.when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));

        // then
        EmailException emailException = assertThrows(EmailException.class, () -> {
            emailService.sendSimpleMessageRegist(email);
        });

        assertEquals("E-004", emailException.getException().getCode());
    }
}