package com.park.restapi.domain.auth.entity;


import com.park.restapi.domain.member.entity.Member;
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
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;
    @Column(length = 200)
    private String value;
    private LocalDateTime expireDate;

    @Builder
    public RefreshToken(Member member, String value, LocalDateTime expireDate) {
        this.member = member;
        this.value = value;
        this.expireDate = expireDate;
    }

    public void addTokenValueAndExpireDate(String value, LocalDateTime expireDate){
        this.value = value;
        this.expireDate = expireDate;
    }

}
