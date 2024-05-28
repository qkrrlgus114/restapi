package com.park.restapi.domain.inquiry.entity;

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
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createDate;

    @Column(nullable = true)
    private LocalDateTime updateDate;

    @OneToOne(mappedBy = "answer", fetch = FetchType.LAZY)
    private Inquiry inquiry;

    @Builder
    public Answer(String content, Inquiry inquiry) {
        this.content = content;
        this.inquiry = inquiry;
    }

    public void updateAnswer(String content){
        this.content = content;
        this.updateDate = LocalDateTime.now();
    }

}
