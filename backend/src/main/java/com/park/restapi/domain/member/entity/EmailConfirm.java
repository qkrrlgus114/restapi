package com.park.restapi.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class EmailConfirm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String certificationNumber;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createDate;

    @Column(nullable = false)
    private Boolean certificationStatus;

    @Builder
    public EmailConfirm(String certificationNumber, Boolean certificationStatus) {
        this.certificationNumber = certificationNumber;
        this.certificationStatus = certificationStatus;
    }


    // 사용으로 변경
    public void changedUsing(){
        this.certificationStatus = true;
    }

}
