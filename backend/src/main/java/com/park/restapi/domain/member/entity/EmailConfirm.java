package com.park.restapi.domain.member.entity;

import com.park.restapi.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailConfirm extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String certificationNumber;

    @Column(nullable = false)
    private Boolean certificationStatus;

    @Builder
    public EmailConfirm(String certificationNumber, Boolean certificationStatus) {
        this.certificationNumber = certificationNumber;
        this.certificationStatus = certificationStatus;
    }

    // 사용으로 변경
    public void changedUsing() {
        this.certificationStatus = true;
    }

}
