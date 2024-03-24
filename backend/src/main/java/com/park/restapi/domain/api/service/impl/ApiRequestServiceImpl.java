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


        String model = dto.getModel();
        String method = dto.getMethodType().toString();
        String content = dto.getContent();
        String resource = dto.getResource();

        // 프롬프트
        String prompt = String.format(
                "다음 정보를 바탕으로 최대한 RESTful한 API 경로를 3개 제안해주세요:\n\n" +
                        "- HTTP 메서드: %s\n" +
                        "- 주체가 되는 자원: %s\n" +
                        "- 작업 설명: %s\n\n" +
                        "제안할 API 경로는 다음 RESTful 디자인 규칙을 따라야 합니다:\n" +
                        "1. 자원을 명사로 나타내고, URI의 마지막에 슬래시(/)는 사용하지 않습니다.\n" +
                        "2. 가독성을 위해 하이픈(-)을 사용하고, 언더스코어(_)는 사용하지 않습니다.\n" +
                        "3. 모두 소문자를 사용합니다.\n" +
                        "4. URI에 파일 확장자는 포함하지 않습니다.\n" +
                        "5. CRUD 함수 이름을 URI에 포함하지 않고, 대신 적절한 HTTP 메서드를 사용합니다.\n" +
                        "6. 자원의 필터링을 위해서는 쿼리 파라미터를 사용합니다.\n\n" +
                        "제안된 API 경로는 쉼표(,)로 구분해서 나열해주세요. 예: [POST] /users, [POST] /users/{id}/posts, [POST] /posts/{id}/comments",
                method, resource, content
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
