package com.park.restapi.domain.coupon.repository;

import com.park.restapi.domain.coupon.entity.CouponSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponSettingRepository extends JpaRepository<CouponSetting, Long> {

    CouponSetting findTopByOrderByIdAsc();
}
