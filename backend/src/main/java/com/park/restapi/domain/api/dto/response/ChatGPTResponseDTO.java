package com.park.restapi.domain.api.dto.response;

import com.park.restapi.domain.api.dto.request.Message;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatGPTResponseDTO {

    private List<Choice> choices;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Choice {
        //gpt 대화 인덱스 번호
        private int index;
        // 지피티로 부터 받은 메세지
        // 여기서 content는 유저의 prompt가 아닌 gpt로부터 받은 response
        private Message message;
    }
}
