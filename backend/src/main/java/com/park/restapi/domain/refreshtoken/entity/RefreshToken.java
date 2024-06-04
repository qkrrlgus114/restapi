package com.park.restapi.domain.refreshtoken.entity;

import com.park.restapi.domain.member.entity.Member;
import com.park.restapi.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = true, length = 200)
    private String accessToken;

    @Column(nullable = true, length = 200)
    private String refreshToken;

    private LocalDateTime expireDate;

    @Builder
    public RefreshToken(Member member, String accessToken, String refreshToken, LocalDateTime expireDate) {
        this.member = member;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expireDate = expireDate;
    }

    public void addTokenValueAndExpireDate(String accessToken, String refreshToken, LocalDateTime expireDate) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expireDate = expireDate;
    }

    public static RefreshToken toEntity(String accessToken, String refreshToken, Member member,
                                        LocalDateTime expireDate) {
        return RefreshToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .member(member)
                .expireDate(expireDate).build();
    }

}
