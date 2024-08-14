package com.park.restapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ChatGPTConfig {

    @Value("${chat-gpt.api-key}")
    private String secretKey;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .defaultHeader("Authorization", "Bearer " + secretKey)
                .build();
    }
}
