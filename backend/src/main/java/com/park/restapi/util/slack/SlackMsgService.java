package com.park.restapi.util.slack;

import com.park.restapi.domain.exception.exception.CommonException;
import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.StringJoiner;

@Service
@Slf4j
public class SlackMsgService {

    @Value("${spring.slack.webhook}")
    private String webhookUrl;

    @Async("slackTaskExecutor")
    public void sendMsg(CommonException e, RequestInfo requestInfo) {
        Slack slack = Slack.getInstance();
        String message = buildMessage(e, requestInfo);

        Payload payload = Payload.builder().text(message).build();

        try {
            slack.send(webhookUrl, payload);
        } catch (IOException ioe) {
            log.error("slack 메시지 발송 중 문제가 발생.");
            throw new RuntimeException(ioe);
        }
    }

    private String buildMessage(CommonException e, RequestInfo requestInfo) {
        StringJoiner sj = new StringJoiner("\n");
        sj.add(":exclamation: *Exception class* :exclamation:")
                .add("```" + e.getClass().getName() + "```")
                .add("*Request URI*")
                .add("```" + requestInfo.requestURI() + "```")
                .add("*Request Method*")
                .add("```" + requestInfo.method() + "```")
                .add("*발생 시간*")
                .add("```" + LocalDateTime.now() + "```")
                .add("*이유*")
                .add("```" + e.getExceptionMessage() + "```")
                .add("*로그메시지*")
                .add("```" + e.getLog() + "```");

        return sj.toString();
    }
}
