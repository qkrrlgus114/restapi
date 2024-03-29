package com.park.restapi.domain.coupon.scheduler;

import com.park.restapi.domain.coupon.entity.Coupon;
import com.park.restapi.domain.coupon.repository.CouponHistoryRepository;
import com.park.restapi.domain.coupon.repository.CouponRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponSchedulerService {
    private final CouponHistoryRepository couponHistoryRepository;
    private final CouponRepository couponRepository;

    private final int COUPON_COUNT = 5;

    // 쿠폰 발급 스케줄러
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    @Transactional(rollbackOn = Exception.class)
    public void generatedCoupon(){
        try{
            log.info("선착순 쿠폰 발행 시작");
            Coupon coupon = Coupon.builder()
                    .remainingQuantity(COUPON_COUNT)
                    .totalQuantity(COUPON_COUNT).build();

            couponRepository.save(coupon);
            log.info("선착순 쿠폰 발행 성공");
        }catch (Exception e){
            log.error("선착순 쿠폰 발행 중 예외 발생", e);
        }
    }
}
