package com.park.restapi.domain.coupon.scheduler;

import com.park.restapi.domain.coupon.entity.Coupon;
import com.park.restapi.domain.coupon.entity.CouponSetting;
import com.park.restapi.domain.coupon.repository.CouponHistoryRepository;
import com.park.restapi.domain.coupon.repository.CouponRepository;
import com.park.restapi.domain.coupon.repository.CouponSettingRepository;
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
    private final CouponSettingRepository couponSettingRepository;

    // 쿠폰 발급 스케줄러
    @Scheduled(cron = "01 0 0 * * *", zone = "Asia/Seoul")
    @Transactional(rollbackOn = Exception.class)
    public void generatedCoupon(){
        try{
            // 쿠폰 설정이 true면 생성
            CouponSetting couponSetting = couponSettingRepository.findAll().get(0);
            if(couponSetting.getIsDailyCouponGenerate()){
                log.info("선착순 쿠폰 발행 시작");
                Coupon coupon = Coupon.builder()
                        .remainingQuantity(couponSetting.getDailyCouponQuantity())
                        .totalQuantity(couponSetting.getDailyCouponQuantity()).build();

                couponRepository.save(coupon);
                log.info("선착순 쿠폰 발행 성공");
            }
        }catch (Exception e){
            log.error("선착순 쿠폰 발행 중 예외 발생", e);
        }
    }
}
