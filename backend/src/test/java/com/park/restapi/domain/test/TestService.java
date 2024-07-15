package com.park.restapi.domain.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

@ExtendWith(MockitoExtension.class)
public class TestService {

    static Semaphore semaphore = new Semaphore(5);

    @Test
    @DisplayName("세마포어 테스트")
    void test() throws InterruptedException {
        int number = 100;
        CountDownLatch latch = new CountDownLatch(number);

        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(100);
        threadPoolTaskExecutor.initialize();

        for (int i = 0; i < number; i++) {
            threadPoolTaskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        semaphore.acquire();
                        Thread.sleep(3000);
                        System.out.println("출력함");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } finally {
                        semaphore.release();
                        latch.countDown();
                    }
                }
            });
        }

        latch.await();
        threadPoolTaskExecutor.shutdown();
    }
}
