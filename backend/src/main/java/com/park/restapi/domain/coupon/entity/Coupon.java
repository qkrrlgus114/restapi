package com.park.restapi.domain.coupon.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Coupon {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Integer remainingQuantity;
    @Column(nullable = false)
    private Integer totalQuantity;
    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createDate;

    @Builder
    public Coupon(Integer remainingQuantity, Integer totalQuantity) {
        this.remainingQuantity = remainingQuantity;
        this.totalQuantity = totalQuantity;
    }

    public void decreasedCoupon(){
        this.remainingQuantity--;
    }
}
