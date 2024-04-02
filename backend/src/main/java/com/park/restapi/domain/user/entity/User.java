package com.park.restapi.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class User {

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
    @Column(nullable = false)
    private LocalDateTime loginLastDate;
    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createDate;
    @Column(nullable = false)
    private Integer token = 3;

    @Builder
    public User(String email, String password, String nickname, LocalDateTime loginLastDate) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.loginLastDate = loginLastDate;
    }

    public User(String email, String nickname, SocialType socialType) {
        this.email = email;
        this.nickname = nickname;
        this.socialType = socialType;
        this.password = String.valueOf(email.hashCode() + nickname.hashCode());
        this.loginLastDate = LocalDateTime.now();
    }

    public static User toEntity(String email, String nickname, SocialType socialType) {
        return new User(email, nickname, socialType);
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
}
