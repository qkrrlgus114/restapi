package com.park.restapi.util.scheduler;

import com.park.restapi.domain.user.entity.User;
import com.park.restapi.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerService {

    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    @Transactional
    public void resetToken(){
        log.info("12시 토큰 리셋 스케줄러 동작");
        List<User> all = userRepository.findAll();
        for(User u : all){
            u.resetToken();
        }
    }
}
