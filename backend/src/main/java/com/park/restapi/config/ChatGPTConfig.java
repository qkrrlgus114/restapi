package com.park.restapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ChatGPTConfig {

    @Value("${chat-gpt.api-key}")
    private String secretKey;

    // 모든 restTemplate 요청에 대해 키를 집어넣는 인터셉터.
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + secretKey);
            return execution.execute(request, body);
        });

        return restTemplate;
    }
}
