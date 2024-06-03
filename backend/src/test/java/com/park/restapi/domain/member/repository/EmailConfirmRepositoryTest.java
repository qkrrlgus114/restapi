package com.park.restapi.domain.member.repository;

import com.park.restapi.domain.config.TestQuerydslConfiguration;
import com.park.restapi.domain.member.entity.EmailConfirm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@Import(TestQuerydslConfiguration.class)
class EmailConfirmRepositoryTest {

    @Autowired
    private EmailConfirmRepository emailConfirmRepository;

    private EmailConfirm savedEmailConfirm;

    @BeforeEach
    void setUp() {
        EmailConfirm emailConfirm = EmailConfirm.builder()
                .certificationNumber("djid#sdfi0")
                .certificationStatus(false).build();

        savedEmailConfirm = emailConfirmRepository.save(emailConfirm);
    }

    @Test
    @DisplayName("사용하지 않고 일치하는 인증번호 검색")
    void checkCode() {
        // given
        String code = "djid#sdfi0";

        // when
        Optional<EmailConfirm> findEmailConfirm = emailConfirmRepository.checkCode(code);

        // then
        Assertions.assertTrue(findEmailConfirm.isPresent());
        Assertions.assertEquals(savedEmailConfirm, findEmailConfirm.get());
    }
}