package com.park.restapi.domain.api.entity;

import com.park.restapi.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiRequestRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @CreatedDate
    private LocalDateTime requestDate;
    @Column(nullable = false)
    private LocalDateTime responseDate;
    @Column(nullable = false)
    private boolean request_status;
    @Column(nullable = false)
    private MethodType methodType;
    @Column(nullable = false)
    private String content;
}
