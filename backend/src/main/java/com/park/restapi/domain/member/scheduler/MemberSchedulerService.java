package com.park.restapi.domain.member.scheduler;

import com.park.restapi.domain.member.entity.Member;
import com.park.restapi.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberSchedulerService {

    private final MemberRepository memberRepository;

    // 매일 12시에 토큰 리셋
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    @Transactional
    public void resetToken(){
        log.info("12시 토큰 리셋 스케줄러 동작");
        List<Member> all = memberRepository.findAll();
        for(Member u : all){
            u.resetToken();
        }
    }
}
