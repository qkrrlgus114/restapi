package com.park.restapi.domain.coupon.entity;

import com.park.restapi.domain.coupon.dto.request.UpdateCouponSettingRequestDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode
public class CouponSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean isDailyCouponGenerate;

    @Column(nullable = false)
    private Integer dailyCouponQuantity;

    public void updateCouponSetting(UpdateCouponSettingRequestDTO requestDTO) {
        this.isDailyCouponGenerate = requestDTO.isDailyCouponGenerate();
        this.dailyCouponQuantity = requestDTO.dailyCouponQuantity();
    }
}
