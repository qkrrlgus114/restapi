package com.park.restapi.domain.member.service.impl;

import com.park.restapi.domain.coupon.repository.CouponRepository;
import com.park.restapi.domain.member.dto.request.SignUpRequestDTO;
import com.park.restapi.domain.member.entity.Member;
import com.park.restapi.domain.member.entity.MemberRole;
import com.park.restapi.domain.member.repository.MemberRepository;
import com.park.restapi.domain.member.repository.MemberRoleRepository;
import com.park.restapi.domain.member.service.MemberService;
import com.park.restapi.util.jwt.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.time.LocalDateTime;

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
    @InjectMocks
    private MemberServiceImpl memberService;


    @Test
    @DisplayName("회원가입에 성공한다.")
    void successSignup() throws IOException, InterruptedException {
        // given
        SignUpRequestDTO signUpRequestDTO = SignUpRequestDTO.builder()
                .email("test@naver.com")
                .password("1234")
                .nickname("테스트").build();
        Member mockMember = Member.builder()
                .email(signUpRequestDTO.getEmail())
                .password("encoded_password")
                .nickname(signUpRequestDTO.getNickname())
                .loginLastDate(LocalDateTime.now()).build();
        MemberRole mockMemberRole = MemberRole.builder()
                .member(mockMember).build();

        when(memberRepository.existsByEmail(any())).thenReturn(false);
        when(memberRepository.save(any(Member.class))).thenReturn(mockMember);
        when(encoder.encode(signUpRequestDTO.getPassword())).thenReturn("encoded_password");

        // when
        memberService.signUp(signUpRequestDTO);

        // then
        verify(memberRepository).existsByEmail(signUpRequestDTO.getEmail());
        verify(encoder).encode(signUpRequestDTO.getPassword());
//        verify(memberRoleRepository).save(mockMemberRole);
    }



}