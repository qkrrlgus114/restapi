package com.park.restapi.domain.coupon.entity;

import com.park.restapi.domain.coupon.dto.request.UpdateCouponSettingRequestDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class CouponSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean isDailyCouponGenerate;

    @Column(nullable = false)
    private int dailyCouponQuantity;

    public void updateCouponSetting(UpdateCouponSettingRequestDTO requestDTO) {
        this.isDailyCouponGenerate = requestDTO.isDailyCouponGenerate();
        this.dailyCouponQuantity = requestDTO.dailyCouponQuantity();
    }
}
