package com.park.restapi.domain.coupon.repository;

import com.park.restapi.domain.coupon.entity.CouponHistory;
import com.park.restapi.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface CouponHistoryRepository extends JpaRepository<CouponHistory, Long> {

    @Query("select ch from CouponHistory ch where ch.createDate >= :startOfDay and ch.createDate <= :endOfDay and ch.user = :user")
    CouponHistory findByCouponHistory(@Param("startOfDay")LocalDateTime startOfDay,
                                      @Param("endOfDay")LocalDateTime endOfDay,
                                      @Param("user") User user);

}
