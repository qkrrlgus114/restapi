package com.park.restapi.domain.refreshtoken.service.impl;

import com.park.restapi.domain.member.entity.Member;
import com.park.restapi.domain.refreshtoken.entity.RefreshToken;
import com.park.restapi.domain.refreshtoken.repository.RefreshTokenRepository;
import com.park.restapi.util.jwt.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceImplTest {
    private static final Long MEMBER_ID = 1L;
    private static final Long REFRESH_TOKEN_ID = 1L;
    private static final String ACCESS_TOKEN = "testAccess";
    private static final String REFRESH_TOKEN = "testRefresh";

    @Mock
    private JwtService jwtService;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private HttpServletResponse response;
    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    Member savedMember;
    RefreshToken savedRefreshToken;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        savedMember = Member.builder()
                .loginLastDate(LocalDateTime.now())
                .password("test")
                .email("test@naver.com")
                .nickname("test")
                .build();
        Field id1 = savedMember.getClass().getDeclaredField("id");
        id1.setAccessible(true);
        id1.set(savedMember, MEMBER_ID);

        savedRefreshToken = RefreshToken.builder()
                .accessToken(ACCESS_TOKEN)
                .refreshToken(REFRESH_TOKEN)
                .expireDate(LocalDateTime.now().plusDays(1))
                .member(savedMember).build();
        Field id2 = savedRefreshToken.getClass().getDeclaredField("id");
        id2.setAccessible(true);
        id2.set(savedRefreshToken, REFRESH_TOKEN_ID);
    }

    @Test
    @DisplayName("토큰 재발급 성공")
    void reGenerateToken() {
        // when
        Mockito.when(refreshTokenRepository.validatedRefreshToken(Mockito.eq(ACCESS_TOKEN), Mockito.eq(REFRESH_TOKEN),
                        Mockito.any(LocalDateTime.class)))
                .thenReturn(savedRefreshToken);
        Mockito.when(jwtService.createAccessToken(MEMBER_ID))
                .thenReturn(ACCESS_TOKEN);
        Mockito.when(jwtService.createRefreshToken(MEMBER_ID, false, ACCESS_TOKEN))
                .thenReturn(REFRESH_TOKEN);

        // when
        refreshTokenService.reGenerateToken(response, ACCESS_TOKEN, REFRESH_TOKEN);

        // then(검증)
        Mockito.verify(refreshTokenRepository).save(Mockito.any(RefreshToken.class));
        Mockito.verify(jwtService).createAccessToken(MEMBER_ID);
        Mockito.verify(jwtService).createRefreshToken(MEMBER_ID, false, ACCESS_TOKEN);

        ArgumentCaptor<Cookie> cookieArgumentCaptor = ArgumentCaptor.forClass(Cookie.class); // 캡쳐할 객체
        Mockito.verify(response, Mockito.times(2)).addCookie(cookieArgumentCaptor.capture()); // 2번 호출됨
        List<Cookie> captorAllValues = cookieArgumentCaptor.getAllValues();
        assertEquals("accessToken", captorAllValues.get(0).getName());
        assertEquals(ACCESS_TOKEN, captorAllValues.get(0).getValue());
        assertEquals("refreshToken", captorAllValues.get(1).getName());
        assertEquals(REFRESH_TOKEN, captorAllValues.get(1).getValue());
    }
}