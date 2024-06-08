package com.park.restapi.domain.inquiry.controller;

import com.park.restapi.domain.inquiry.dto.request.InquiryRequestDTO;
import com.park.restapi.domain.inquiry.dto.response.InquiryInfoResponseDTO;
import com.park.restapi.domain.inquiry.dto.response.InquiryListResponseDTO;
import com.park.restapi.domain.inquiry.service.InquiryService;
import com.park.restapi.domain.member.entity.Member;
import com.park.restapi.util.resolver.CurrentMember;
import com.park.restapi.util.resolver.CurrentMemberFetchRole;
import com.park.restapi.util.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inquiries")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    // 문의 등록하기
    @PostMapping()
    public ResponseEntity<ApiResponse<Void>> createInquiry(@RequestBody @Valid InquiryRequestDTO inquiryRequestDTO,
                                                           @CurrentMember Member member) {
        inquiryService.inquiryRegister(inquiryRequestDTO, member);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.createSuccessNoContent("문의 등록이 완료되었습니다."));
    }

    // 현재 유저의 문의 내역 가져오기
    @GetMapping()
    public ResponseEntity<ApiResponse<InquiryListResponseDTO>> getInquiries(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                            @CurrentMemberFetchRole Member member) {
        InquiryListResponseDTO myInquiries = inquiryService.getMyInquiries(page - 1, member);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.createSuccess(myInquiries, "내 문의내역 가져오기 성공"));
    }

    // 상세 문의내역 가져오기
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InquiryInfoResponseDTO>> getTargetInquiry(@PathVariable(name = "id") Long inquiryId,
                                                                                @CurrentMemberFetchRole Member member) {
        InquiryInfoResponseDTO responseDTO = inquiryService.getTargetInquiry(inquiryId, member);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.createSuccess(responseDTO, inquiryId + "번 문의내역 가져오기 성공"));
    }

}
