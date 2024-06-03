package com.park.restapi.domain.api.service.impl;

import com.park.restapi.domain.api.dto.request.ApiRequestDTO;
import com.park.restapi.domain.api.dto.request.ChatGPTRequestDTO;
import com.park.restapi.domain.api.dto.request.Message;
import com.park.restapi.domain.api.dto.response.ApiRequestHistoryListResponseDTO;
import com.park.restapi.domain.api.dto.response.ApiRequestHistoryResponseDTO;
import com.park.restapi.domain.api.dto.response.ChatGPTResponseDTO;
import com.park.restapi.domain.api.entity.ApiRequestHistory;
import com.park.restapi.domain.api.repository.ApiRequestHistoryRepository;
import com.park.restapi.domain.api.service.ApiRequestService;
import com.park.restapi.domain.exception.exception.GPTException;
import com.park.restapi.domain.exception.exception.MemberException;
import com.park.restapi.domain.exception.info.GPTExceptionInfo;
import com.park.restapi.domain.exception.info.MemberExceptionInfo;
import com.park.restapi.domain.member.entity.Member;
import com.park.restapi.domain.member.entity.Role;
import com.park.restapi.domain.member.repository.MemberRepository;
import com.park.restapi.util.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiRequestServiceImpl implements ApiRequestService {

    private final RestTemplate restTemplate;
    @Value("${openai.url.prompt}")
    private String URL;
    private final MemberRepository memberRepository;
    private final ApiRequestHistoryRepository apiRequestHistoryRepository;
    private final JwtService jwtService;

    private final Semaphore semaphore = new Semaphore(5);

    private static final int DEFAULT_DATA_COUNT = 10;

    // ChatGPT API 호출
    @Override
    @Transactional
    public ChatGPTResponseDTO chatGpt(ApiRequestDTO apiRequestDTO) {

        Member member = null;

        try {
            semaphore.acquire();

            member = getCurrentMember();

            if (member.getToken() <= 0) {
                throw new MemberException(MemberExceptionInfo.NO_REMAINING_USES, "토큰 부족");
            }

            String model = apiRequestDTO.model();
            String method = apiRequestDTO.methodType().toString();
            String content = apiRequestDTO.content();
            String resource = apiRequestDTO.resource();

            // 프롬프트
            String prompt = String.format(
                    "다음 정보를 바탕으로 최대한 RESTFUL 규칙을 가진 API 경로를 3개 제안해주세요:\n\n" + "- HTTP 메서드: %s\n" + "- 주체가 되는 자원: %s\n"
                            + "- 작업 설명: %s\n\n" + "제안할 API 경로는 다음 RESTFUL 디자인 규칙을 따라야 합니다:\n"
                            + "1. 자원을 명사로 나타내고, URI 의 마지막에 슬래시(/)는 사용하지 않습니다.\n"
                            + "2. 가독성을 위해 하이픈(-)을 사용하고, 언더스코어(_)는 사용하지 않습니다.\n" + "3. 모두 소문자를 사용합니다.\n"
                            + "4. URI 에 파일 확장자는 포함하지 않습니다.\n" + "5. CRUD 함수 이름을 URI 에 포함하지 않고, 대신 적절한 HTTP 메서드를 사용합니다.\n"
                            + "6. 자원의 필터링을 위해서는 쿼리 파라미터를 사용합니다.\n\n"
                            + "제안된 API 경로는 쉼표(,)로 구분해서 나열해주세요. 예: [POST] /users, [POST] /users/{id}/posts, [POST] /posts/{id}/comments\n\n"
                            + "답변에는 API의 경로만 있어야 합니다.", method, resource, content);

            List<Message> messages = new ArrayList<>();
            Message message = Message.builder().role("user").content(prompt).build();
            messages.add(message);

            ChatGPTRequestDTO requestDTO = ChatGPTRequestDTO.builder().model(model).messages(messages).build();

            ChatGPTResponseDTO chatGPTResponseDTO = restTemplate.postForObject(URL, requestDTO,
                    ChatGPTResponseDTO.class);

            // 응답이 왔다면
            ApiRequestHistory apiRequestHistory = apiRequestDTO.toEntity(chatGPTResponseDTO, member, true);
            apiRequestHistoryRepository.save(apiRequestHistory);

            member.useToken();

            return chatGPTResponseDTO;
        } catch (InterruptedException e) {
            throw new GPTException(GPTExceptionInfo.FAIL_INTERRUPTED, e.getMessage());
        } catch (HttpClientErrorException.TooManyRequests e) {
            ApiRequestHistory entity = apiRequestDTO.toEntity(null, member, false);
            apiRequestHistoryRepository.save(entity);
            throw new GPTException(GPTExceptionInfo.FAIL_REQUEST_GPT, e.getMessage());
        } finally {
            semaphore.release();
        }
    }

    // API 요청 기록 조회
    @Override
    @Transactional(readOnly = true)
    public ApiRequestHistoryListResponseDTO getApiRequestHistory(int page, String searchType, String keyword) {
        Member currentMember = getCurrentMember();

        if (!isAdmin(currentMember)) {
            throw new MemberException(MemberExceptionInfo.USER_NOT_ADMIN,
                    currentMember.getEmail() + " 유저가 api 요청 이력 조회를 시도했습니다.(관리자 아님)");
        }

        Pageable pageRequest = PageRequest.of(page, DEFAULT_DATA_COUNT);
        Page<ApiRequestHistoryResponseDTO> apiRequestHistories = null;

        if (searchType != null && keyword != null && !searchType.isEmpty() && !keyword.isEmpty()) {
            apiRequestHistories = apiRequestHistoryRepository.searchApiRequestHistoryByCondition(pageRequest,
                    searchType, keyword);
        } else {
            apiRequestHistories = apiRequestHistoryRepository.searchApiRequestHistory(pageRequest);
        }

        return ApiRequestHistoryListResponseDTO.builder()
                .apiRequestHistoryResponseDTOS(apiRequestHistories.getContent())
                .currentPage(apiRequestHistories.getNumber())
                .totalPages(apiRequestHistories.getTotalPages())
                .build();
    }

    // 현재 로그인 유저 찾기
    private Member getCurrentMember() {
        Long currentUserId = jwtService.getCurrentUserId();
        return memberRepository.findById(currentUserId)
                .orElseThrow(
                        () -> new MemberException(MemberExceptionInfo.NOT_FOUND_MEMBER, currentUserId + "번 유저를 찾지 못했습니다."));
    }

    // 관리자 권한 확인
    private boolean isAdmin(Member member) {
        return member.getMemberRoles().stream().anyMatch(role -> role.getRole().equals(Role.ADMIN));
    }
}