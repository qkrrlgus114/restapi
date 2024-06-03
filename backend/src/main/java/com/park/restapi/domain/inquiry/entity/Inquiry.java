package com.park.restapi.domain.inquiry.entity;

import com.park.restapi.domain.inquiry.dto.request.InquiryRequestDTO;
import com.park.restapi.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createDate;

    @Column(nullable = false)
    private boolean isAnswered = false;

    @Column(nullable = false)
    private boolean emailSendCheck = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InquiryCategory inquiryCategory;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    private Answer answer;

    @Builder
    public Inquiry(Member member, String title, String content, InquiryCategory inquiryCategory,
                   boolean emailSendCheck) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.isAnswered = false;
        this.inquiryCategory = inquiryCategory;
        this.emailSendCheck = emailSendCheck;
    }

    public static Inquiry toEntity(InquiryRequestDTO inquiryRequestDTO, Member member) {
        return Inquiry.builder()
                .member(member)
                .title(inquiryRequestDTO.title())
                .content(inquiryRequestDTO.content())
                .emailSendCheck(inquiryRequestDTO.emailSendCheck())
                .inquiryCategory(inquiryRequestDTO.inquiryCategory()).build();
    }

    public void registerAnswer(Answer answer) {
        this.isAnswered = true;
        this.answer = answer;
    }

}
