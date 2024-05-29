package com.park.restapi.domain.member.scheduler;

import com.park.restapi.domain.member.entity.Member;
import com.park.restapi.domain.member.entity.WithdrawalMember;
import com.park.restapi.domain.member.repository.MemberRepository;
import com.park.restapi.domain.member.repository.WithdrawalMemberRepository;
import com.park.restapi.domain.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberSchedulerService {

    private final MemberService memberService;
    private final WithdrawalMemberRepository withdrawalMemberRepository;

    // 매일 12시에 토큰 리셋
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    @Transactional
    public void resetToken(){
        log.info("오전 12시 토큰 리셋 스케줄러 동작");

        memberService.resetAllTokens();
    }

    // 매일 오전 3시에 탈퇴 유저 30일 지났는지 판단
    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Seoul")
    @Transactional
    public void withdrawalMember(){
        log.info("오전 3시 유저 탈퇴 스케줄러 동작");

        memberService.withdrawalMember();
    }
}
