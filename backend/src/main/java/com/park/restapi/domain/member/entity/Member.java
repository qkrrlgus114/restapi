package com.park.restapi.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 10)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(nullable = true)
    private LocalDateTime loginLastDate;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createDate;

    @Column(nullable = true)
    private LocalDateTime bannedDate;
    
    @Column(nullable = true)
    private LocalDateTime withdrawalDate;


    @Column(nullable = false)
    private Integer token = 3;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    private List<MemberRole> memberRoles = new ArrayList<>();

    @Builder
    public Member(String email, String password, String nickname, LocalDateTime loginLastDate, SocialType socialType) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.loginLastDate = loginLastDate;
        this.socialType = socialType;
    }

    public Member(String email, String nickname, SocialType socialType) {
        this.email = email;
        this.nickname = nickname;
        this.socialType = socialType;
        this.password = String.valueOf(email.hashCode() + nickname.hashCode());
        this.loginLastDate = LocalDateTime.now();
    }

    public void useToken(){
        this.token -= 1;
    }

    public void resetToken(){
        this.token = token < 4 ? 3 : this.token;
    }

    public void updateLoginDate(){
        this.loginLastDate = LocalDateTime.now();
    }

    // 토큰 획득
    public void increasedToken(){
        this.token++;
    }

    // 탈퇴 시간 추가
    public void updateWithdrawalDate(){
        this.withdrawalDate = LocalDateTime.now();
    }

}
