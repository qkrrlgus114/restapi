package com.park.restapi.domain.api.service.impl;

import com.park.restapi.domain.api.dto.request.ApiRequestDTO;
import com.park.restapi.domain.api.dto.request.ChatGPTRequestDTO;
import com.park.restapi.domain.api.dto.request.Message;
import com.park.restapi.domain.api.dto.response.ChatGPTResponseDTO;
import com.park.restapi.domain.api.service.ApiRequestService;
import com.park.restapi.domain.exception.exception.UserException;
import com.park.restapi.domain.exception.info.UserExceptionInfo;
import com.park.restapi.domain.user.entity.User;
import com.park.restapi.domain.user.repository.UserRepository;
import com.park.restapi.util.jwt.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiRequestServiceImpl implements ApiRequestService {

    private final RestTemplate restTemplate;
    @Value("${openai.url.prompt}")
    private String URL;

    //api key를 application.yml에 넣어두었습니다.
    @Value("${chat-gpt.api-key}")
    private String apiKey;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ChatGPTResponseDTO chatGpt(ApiRequestDTO dto) {

        Long currentUserId = JwtService.getCurrentUserId();
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserException(UserExceptionInfo.NOT_FOUND_USER, "유저 데이터 없음"));

        if(user.getToken() <= 0){
            throw new UserException(UserExceptionInfo.NO_REMAINING_USES, "토큰 부족");
        }


        String operation = dto.getOperation();
        String model = dto.getModel();
        String method = dto.getMethodType().toString();
        String content = dto.getContent();
        String resource = dto.getResource();

        // 프롬프트 생성
        String prompt = String.format(
                        "수행 작업 : '%s'\n" +
                        "수행 메서드 : '%s' \n" +
                        "수행 주체 : '%s' \n"+
                        "추가 설명 : '%s' \n" +
                        "위의 4가지 정보를 이용해서 최대한 restFul한 API 경로를 3개 뽑아줘.\n" +
                                "RESTAPI는 아래 8가지 규칙을 지켜서 만들어줘.\n" +
                                "1.자원을 명사로 나타내라\n" +
                                "2.URI의 마지막에 ‘/’를 사용 하지 않는다.\n" +
                                "3.‘-’(하이픈)을 사용하여 가독성을 높인다.\n" +
                                "4.‘_’(언더스코어)는 사용하지 않는다.\n" +
                                "5.소문자를 사용 한다.\n" +
                                "6. 파일의 확장자는 사용하지 않는다.\n" +
                                "7. CRUD함수 명을 사용하지 않는다. \n" +
                                "8. 자원의 필터링을 위해서는 쿼리 파라미터를 사용한다." +
                "해당 결과는 쉼표로 구분해서 api 경로만 응답으로 돌려주면 돼.",
                operation, method, resource, content
        );

        Message message = Message.builder()
                .role("user")
                .content(prompt).build();

        List<Message> messages = new ArrayList<>();
        messages.add(message);

        ChatGPTRequestDTO requestDTO = ChatGPTRequestDTO.builder()
                .model(model)
                .messages(messages).build();

        ChatGPTResponseDTO chatGPTResponseDTO = restTemplate.postForObject(
                URL
                , requestDTO
                , ChatGPTResponseDTO.class);

        user.useToken();

        return chatGPTResponseDTO;
    }
}
