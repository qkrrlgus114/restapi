package com.park.restapi.domain.user.entity;

import com.park.restapi.domain.api.entity.ApiRequestRecord;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private LocalDateTime withdrawDate;
    @OneToMany(mappedBy = "user")
    private List<ApiRequestRecord> apiRequestRecordList = new ArrayList<>();
    @Column(nullable = false)
    private int token = 2;

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
        this.token = 2;
    }
}
