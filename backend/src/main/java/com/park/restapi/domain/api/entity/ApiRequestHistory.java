package com.park.restapi.domain.api.entity;

import com.park.restapi.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// API 요청 이력 엔티티
@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class ApiRequestHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @CreatedDate
    private LocalDateTime requestDate;

    @Column(nullable = false)
    private boolean requestStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MethodType methodType;

    @Column(nullable = false)
    private String requestContent;

    private String responseContent;

    @Builder
    public ApiRequestHistory(Member member, boolean requestStatus, MethodType methodType, String requestContent, String responseContent) {
        this.member = member;
        this.requestStatus = requestStatus;
        this.methodType = methodType;
        this.requestContent = requestContent;
        this.responseContent = responseContent;
    }
}
