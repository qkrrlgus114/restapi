package com.park.restapi.domain.member.service.impl;

import com.park.restapi.domain.exception.exception.EmailException;
import com.park.restapi.domain.member.entity.EmailConfirm;
import com.park.restapi.domain.member.repository.EmailConfirmRepository;
import com.park.restapi.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.lang.reflect.Field;
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
    @InjectMocks
    private EmailServiceImpl emailService;

    EmailConfirm emailConfirm;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        emailConfirm = EmailConfirm.builder()
                .certificationNumber("123456")
                .certificationStatus(false).build();
        Field createDate = emailConfirm.getClass().getDeclaredField("createDate");
        createDate.setAccessible(true);
        createDate.set(emailConfirm, LocalDateTime.now());
    }

    @Test
    @DisplayName("인증번호 생성")
    void createKey() {
        // when
        String key = emailService.createKey();

        // then
        Assertions.assertEquals(10, key.length());
        Assertions.assertTrue(key.matches("[A-Za-z0-9!@#$%^&*()]+"));
    }

    @Test
    void sendSimpleMessageChange() {
    }

    @Test
    void sendSimpleMessageRegist() {
    }

    @Test
    void 인증번호_사용_성공() {
        // given
        String code = "123456";
        Mockito.when(emailConfirmRepository.checkCode(code)).thenReturn(Optional.ofNullable(emailConfirm));

        // when
        emailService.checkCertificationCode(code);

        // then
        Assertions.assertTrue(emailConfirm.getCertificationStatus());
    }

    @Test
    void 인증번호_불일치(){
        // given
        String code = "654321";
        Mockito.when(emailConfirmRepository.checkCode(code)).thenReturn(Optional.empty());

        // then
        EmailException emailException = assertThrows(EmailException.class, () -> {
            emailService.checkCertificationCode(code);
        });
        Assertions.assertEquals("인증번호가 일치하지 않습니다", emailException.getException().getMessage());
    }

    @Test
    void 인증번호_만료() throws NoSuchFieldException, IllegalAccessException {
        // given
        Field createDate = emailConfirm.getClass().getDeclaredField("createDate");
        createDate.setAccessible(true);
        createDate.set(emailConfirm, LocalDateTime.now().minusMinutes(5));
        String code = "123456";
        Mockito.when(emailConfirmRepository.checkCode(code)).thenReturn(Optional.ofNullable(emailConfirm));

        // then
        EmailException emailException = assertThrows(EmailException.class, () -> {
            emailService.checkCertificationCode(code);
        });
        Assertions.assertEquals("인증번호가 만료되었습니다.", emailException.getException().getMessage());
    }
}