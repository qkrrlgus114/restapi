package com.park.restapi.domain.inquiry.entity;

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
public class Answer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = true)
    private LocalDateTime updateDate;

    @Builder
    public Answer(String content) {
        this.content = content;
    }

    public void updateAnswer(String content) {
        this.content = content;
        this.updateDate = LocalDateTime.now();
    }

}
