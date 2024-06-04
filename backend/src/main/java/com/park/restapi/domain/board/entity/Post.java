package com.park.restapi.domain.board.entity;

import com.park.restapi.domain.member.entity.Member;
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
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = true)
    private LocalDateTime deleteDate;

    @Column(nullable = false)
    private Integer likeCount = 0;

    @Builder
    public Post(Member member, BoardType boardType, String title, String content) {
        this.member = member;
        this.boardType = boardType;
        this.title = title;
        this.content = content;
    }
}