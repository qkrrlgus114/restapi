package com.park.restapi.domain.member.service.impl;

import com.park.restapi.domain.coupon.repository.CouponRepository;
import com.park.restapi.domain.member.repository.MemberRepository;
import com.park.restapi.domain.member.repository.MemberRoleRepository;
import com.park.restapi.domain.member.service.MemberService;
import com.park.restapi.util.jwt.JwtService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

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
    private MemberService memberService;



}