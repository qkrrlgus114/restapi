package com.park.restapi;

import com.zaxxer.hikari.HikariConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;
import java.util.concurrent.ThreadPoolExecutor;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableAsync
public class RestapiApplication {

	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}

	public static void main(String[] args) {
		SpringApplication.run(RestapiApplication.class, args);
	}

}
