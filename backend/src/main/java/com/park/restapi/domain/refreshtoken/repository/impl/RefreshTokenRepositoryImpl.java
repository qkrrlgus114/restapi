package com.park.restapi.domain.refreshtoken.repository.impl;

import com.park.restapi.domain.refreshtoken.entity.RefreshToken;
import com.park.restapi.domain.refreshtoken.repository.RefreshTokenCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import static com.park.restapi.domain.member.entity.QMember.member;
import static com.park.restapi.domain.refreshtoken.entity.QRefreshToken.refreshToken1;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public RefreshToken validatedRefreshToken(String accessTokenValue, String refreshTokenValue, LocalDateTime currentDate) {

        RefreshToken refreshToken = queryFactory.selectFrom(refreshToken1)
                .leftJoin(refreshToken1.member, member).fetchJoin()
                .where(refreshToken1.accessToken.eq(accessTokenValue)
                        .and(refreshToken1.refreshToken.eq(refreshTokenValue))
                        .and(refreshToken1.expireDate.after(currentDate))
                )
                .fetchFirst();

        return refreshToken;
    }
}
