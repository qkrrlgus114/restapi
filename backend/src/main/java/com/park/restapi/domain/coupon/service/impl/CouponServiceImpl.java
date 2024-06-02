package com.park.restapi.domain.coupon.service.impl;

import com.park.restapi.domain.coupon.dto.request.UpdateCouponQuantityRequestDTO;
import com.park.restapi.domain.coupon.dto.request.UpdateCouponSettingRequestDTO;
import com.park.restapi.domain.coupon.dto.response.CouponSettingResponseDTO;
import com.park.restapi.domain.coupon.entity.Coupon;
import com.park.restapi.domain.coupon.entity.CouponHistory;
import com.park.restapi.domain.coupon.entity.CouponSetting;
import com.park.restapi.domain.coupon.repository.CouponHistoryRepository;
import com.park.restapi.domain.coupon.repository.CouponRepository;
import com.park.restapi.domain.coupon.repository.CouponSettingRepository;
import com.park.restapi.domain.coupon.service.CouponService;
import com.park.restapi.domain.exception.exception.CouponException;
import com.park.restapi.domain.exception.exception.MemberException;
import com.park.restapi.domain.exception.info.CouponExceptionInfo;
import com.park.restapi.domain.exception.info.MemberExceptionInfo;
import com.park.restapi.domain.member.entity.Member;
import com.park.restapi.domain.member.repository.MemberRepository;
import com.park.restapi.util.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponHistoryRepository couponHistoryRepository;
    private final CouponSettingRepository couponSettingRepository;
    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    // 쿠폰 획득
    @Override
    @Transactional
    public void acquisitionCoupon() {
        Member member = getCurrentMember();

        // 오늘 획득한 이력이 있으면 중복 불가.
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        CouponHistory byCouponHistory = couponHistoryRepository.findByCouponHistory(startOfDay, endOfDay, member);

        if (byCouponHistory != null) {
            throw new MemberException(MemberExceptionInfo.ALREADY_GET_COUPON, "이미 쿠폰 획득 완료.");
        }

        // 쿠폰이 남아있는지 확인
        Coupon coupon = couponRepository.findByCouponForWrite(startOfDay, endOfDay)
                .orElseThrow(() -> new CouponException(CouponExceptionInfo.FAIL_COUPON_DATA, "DB에 쿠폰 데이터 존재하지 않음."));

        if (coupon.getRemainingQuantity() < 1) {
            throw new CouponException(CouponExceptionInfo.NOT_EXIST_COUPON, "쿠폰 품절");
        }
        // 쿠폰 감소
        coupon.decreasedCoupon();

        // 유저 토큰 + 1
        member.increasedToken();

        CouponHistory couponHistory = CouponHistory.builder()
                .member(member).build();
        couponHistoryRepository.save(couponHistory);
        log.info("쿠폰 획득 성공");
    }

    // 남은 선착순 쿠폰 조회
    @Override
    @Transactional(readOnly = true)
    public int getCoupons() {
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);

        return couponRepository.findCouponForRead(startOfDay, endOfDay)
                .map(Coupon::getRemainingQuantity)
                .orElse(0);
    }

    // 쿠폰 설정 가져오기
    @Override
    @Transactional(readOnly = true)
    public CouponSettingResponseDTO getCouponSetting() {
        CouponSetting couponSetting = couponSettingRepository.findTopByOrderByIdAsc();

        return CouponSettingResponseDTO.fromEntity(couponSetting);
    }

    // 쿠폰 설정 업데이트
    @Override
    @Transactional
    public void updateCouponSetting(UpdateCouponSettingRequestDTO requestDTO) {
        CouponSetting couponSetting = couponSettingRepository.findTopByOrderByIdAsc();

        couponSetting.updateCouponSetting(requestDTO);
    }

    // 쿠폰 개수 업데이트
    @Override
    @Transactional
    public int updateCouponQuantity(UpdateCouponQuantityRequestDTO requestDTO) {
        Coupon coupon = null;

        // 오늘 발급된 쿠폰이 있는지 확인
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);

        Optional<Coupon> byCouponForWrite = couponRepository.findByCouponForWrite(startOfDay, endOfDay);

        // 발급된 쿠폰이 없으면 새로 발급
        if (byCouponForWrite.isEmpty()) {
            coupon = Coupon.builder()
                    .totalQuantity(requestDTO.getDailyCouponQuantity())
                    .remainingQuantity(requestDTO.getDailyCouponQuantity()).build();
            couponRepository.save(coupon);
        } else {
            coupon = byCouponForWrite.get();
            coupon.updateCouponQuantity(requestDTO.getDailyCouponQuantity());
        }

        return coupon.getRemainingQuantity();
    }

    // 현재 로그인 유저 찾기
    private Member getCurrentMember() {
        Long currentUserId = jwtService.getCurrentUserId();
        return memberRepository.findById(currentUserId)
                .orElseThrow(() -> new MemberException(MemberExceptionInfo.NOT_FOUND_MEMBER, currentUserId + "번 유저를 찾지 못했습니다."));
    }
}
