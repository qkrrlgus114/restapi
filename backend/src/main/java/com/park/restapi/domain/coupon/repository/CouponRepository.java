package com.park.restapi.domain.coupon.repository;

import com.park.restapi.domain.coupon.entity.Coupon;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    // 당일 쿠폰 조회
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Coupon c where c.createdDate >= :startOfDay and c.createdDate <= :endOfDay")
    Optional<Coupon> findByCouponForWrite(@Param("startOfDay") LocalDateTime startOfDay,
                                          @Param("endOfDay") LocalDateTime endOfDay);

    @Query("select c from Coupon c where c.createdDate >= :startOfDay and c.createdDate <= :endOfDay")
    Optional<Coupon> findCouponForRead(@Param("startOfDay") LocalDateTime startOfDay,
                                       @Param("endOfDay") LocalDateTime endOfDay);
}
