package com.park.restapi.domain.refreshtoken.repository;

import com.park.restapi.domain.config.TestQuerydslConfiguration;
import com.park.restapi.domain.member.entity.Member;
import com.park.restapi.domain.member.repository.MemberRepository;
import com.park.restapi.domain.refreshtoken.entity.RefreshToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@Import(TestQuerydslConfiguration.class)
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private MemberRepository memberRepository;

    String testAccessToken = "testAccess";
    String testRefreshToken = "testRefresh";
    RefreshToken savedRefreshToken;
    Member savedMember;

    @BeforeEach
    void setUp() {
        savedMember = Member.builder()
                .loginLastDate(LocalDateTime.now())
                .password("test")
                .email("test@naver.com")
                .nickname("test")
                .build();

        memberRepository.save(savedMember);

        savedRefreshToken = RefreshToken.builder()
                .accessToken(testAccessToken)
                .refreshToken(testRefreshToken)
                .expireDate(LocalDateTime.now().plusDays(1))
                .member(savedMember).build();

        refreshTokenRepository.save(savedRefreshToken);
    }

    @Test
    @DisplayName("액세스, 리프레시로 만료되지 않은 토큰 조회 성공")
    void validatedRefreshToken() {
        // given
        String accessToken = testAccessToken;

        // when
        RefreshToken findRefreshToken = refreshTokenRepository.validatedRefreshToken(accessToken, testRefreshToken, LocalDateTime.now());

        // then
        assertNotNull(findRefreshToken);
        assertEquals(savedRefreshToken, findRefreshToken);
        assertEquals(savedMember, findRefreshToken.getMember());
    }

    @Test
    @DisplayName("액세스, 리프레시와 일치하는 토큰 없음")
    void validatedRefreshTokenFail() {
        // given
        String accessToken = testAccessToken;
        String refreshToken = "testRefreshFail"; // 일치하지 않는 리프레시 토큰

        // when
        RefreshToken findRefreshToken = refreshTokenRepository.validatedRefreshToken(accessToken, refreshToken, LocalDateTime.now());

        // then
        assertNull(findRefreshToken);
    }

    @Test
    @DisplayName("이미 만료된 토큰")
    void validatedRefreshTokenExpired() {
        // given
        String accessToken = testAccessToken;

        // when
        RefreshToken refreshToken = refreshTokenRepository.validatedRefreshToken(accessToken, testRefreshToken, LocalDateTime.now().plusDays(2));

        // then
        assertNull(refreshToken);
    }
}