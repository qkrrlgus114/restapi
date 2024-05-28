package com.park.restapi.domain.inquiry.service.impl;

import com.park.restapi.domain.exception.exception.AnswerException;
import com.park.restapi.domain.exception.exception.InquiryException;
import com.park.restapi.domain.exception.exception.MemberException;
import com.park.restapi.domain.exception.info.AnswerExceptionInfo;
import com.park.restapi.domain.exception.info.InquiryExceptionInfo;
import com.park.restapi.domain.exception.info.MemberExceptionInfo;
import com.park.restapi.domain.inquiry.dto.request.InquiryRequestDTO;
import com.park.restapi.domain.inquiry.dto.response.InquiryInfoResponseDTO;
import com.park.restapi.domain.inquiry.dto.response.InquiryListResponseDTO;
import com.park.restapi.domain.inquiry.dto.response.InquiryResponseDTO;
import com.park.restapi.domain.inquiry.entity.Answer;
import com.park.restapi.domain.inquiry.entity.Inquiry;
import com.park.restapi.domain.inquiry.repository.AnswerRepository;
import com.park.restapi.domain.inquiry.repository.InquiryRepository;
import com.park.restapi.domain.inquiry.service.InquiryService;
import com.park.restapi.domain.member.entity.Member;
import com.park.restapi.domain.member.entity.MemberRole;
import com.park.restapi.domain.member.entity.Role;
import com.park.restapi.domain.member.repository.MemberRepository;
import com.park.restapi.util.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InquiryServiceImpl implements InquiryService {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final InquiryRepository inquiryRepository;
    private final AnswerRepository answerRepository;

    // 문의 등록하기
    @Override
    @Transactional
    public void inquiryRegister(InquiryRequestDTO inquiryRequestDTO) {
        Member currentMember = getCurrentMember();

        Inquiry inquiry = Inquiry.toEntity(inquiryRequestDTO, currentMember);
        inquiryRepository.save(inquiry);
    }

    // 내 모든 문의 가져오기
    @Override
    @Transactional(readOnly = true)
    public InquiryListResponseDTO getMyInquiries() {
        Member currentMember = getCurrentMember();

        List<Inquiry> inquiryList = inquiryRepository.findByMember(currentMember);

        List<InquiryResponseDTO> inquiryResponseDTOS = inquiryList.stream()
                .map(InquiryResponseDTO::toDTO)
                .collect(Collectors.toList());

        return InquiryListResponseDTO.builder()
                .inquiryResponseDTOS(inquiryResponseDTOS)
                .totalCount(inquiryList.size()).build();
    }

    // 질문 상세내용 가져오기
    @Override
    @Transactional(readOnly = true)
    public InquiryInfoResponseDTO getTargetInquiry(Long inquiryId) {
        Member currentMember = getCurrentMember();
        Answer answer = null;

        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new InquiryException(InquiryExceptionInfo.NOT_FOUND_INQUIRY, inquiryId + "번 문의 내역을 찾을 수 없습니다."));

        if (!inquiry.getMember().equals(currentMember) && !isAdmin(currentMember)){
            throw new InquiryException(InquiryExceptionInfo.NOT_MATCH_MEMBER, currentMember.getEmail() + " 유저가 " + inquiryId + "질문에 접근했습니다.(접근 차단)");
        }

        if (inquiry.isAnswered()) {
            answer = answerRepository.findByInquiry(inquiry)
                    .orElseThrow(() -> new AnswerException(AnswerExceptionInfo.NOT_FOUND_ANSWER, inquiryId + "번 문의 답변 내역을 찾을 수 없습니다."));
        }

        return InquiryInfoResponseDTO.toDTO(answer, inquiry);
    }

    // 현재 로그인 유저 찾기
    private Member getCurrentMember() {
        Long currentUserId = jwtService.getCurrentUserId();

        return memberRepository.findById(currentUserId)
                .orElseThrow(() -> new MemberException(MemberExceptionInfo.NOT_FOUND_MEMBER, currentUserId + "번 유저를 찾지 못했습니다."));
    }

    // 관리자 권한 확인
    private boolean isAdmin(Member member){
        return member.getMemberRoles().stream()
                .anyMatch(role -> role.getRole().equals(Role.ADMIN));
    }
}
