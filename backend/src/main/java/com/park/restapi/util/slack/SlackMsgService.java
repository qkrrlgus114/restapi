package com.park.restapi.util.slack;

import com.park.restapi.domain.exception.exception.CommonException;
import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@Slf4j
public class SlackMsgService {

    @Value("${spring.slack.webhook}")
    private String webhookUrl;

    @Async("slackTaskExecutor")
    public void sendMsg(CommonException e, HttpServletRequest request) {
        Slack slack = Slack.getInstance();
        String message = buildMessage(e, request);

        Payload payload = Payload.builder().text(message).build();

        try {
            slack.send(webhookUrl, payload);
        } catch (IOException ioe) {
            log.error("slack 메시지 발송 중 문제가 발생.");
            throw new RuntimeException(ioe);
        }
    }

    private String buildMessage(CommonException e, HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(":exclamation: *Exception class* :exclamation:").append("\n")
                .append("```").append(e.getClass().getName()).append("```").append("\n")
                .append("*Request URI*").append("\n")
                .append("```").append(request.getRequestURI()).append("```").append("\n")
                .append("*Request Method*").append("\n")
                .append("```").append(request.getMethod()).append("```").append("\n")
                .append("*발생 시간*").append("\n")
                .append("```").append(LocalDateTime.now()).append("```").append("\n")
                .append("*이유*").append("\n")
                .append("```").append(e.getExceptionMessage()).append("```").append("\n")
                .append("*로그메시지*").append("\n")
                .append("```").append(e.getLog()).append("```");
        return sb.toString();
    }
}
