package com.park.restapi.domain.inquiry.controller;

import com.park.restapi.domain.inquiry.dto.request.InquiryRequestDTO;
import com.park.restapi.domain.inquiry.dto.response.InquiryInfoResponseDTO;
import com.park.restapi.domain.inquiry.dto.response.InquiryListResponseDTO;
import com.park.restapi.domain.inquiry.service.InquiryService;
import com.park.restapi.util.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    // 문의 등록하기
    @PostMapping("inquiries")
    public ResponseEntity<ApiResponse<?>> createInquiry(@RequestBody @Valid InquiryRequestDTO inquiryRequestDTO){
        inquiryService.inquiryRegister(inquiryRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.createSuccessNoContent("문의 등록이 완료되었습니다."));
    }

    // 현재 유저의 문의 내역 가져오기
    @GetMapping("inquiries")
    public ResponseEntity<ApiResponse<?>> getInquiries(@RequestParam(value = "page", defaultValue = "1") int page){
        InquiryListResponseDTO myInquiries = inquiryService.getMyInquiries(page - 1);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.createSuccess(myInquiries, "내 문의내역 가져오기 성공"));
    }
    
    // 상세 문의내역 가져오기
    @GetMapping("inquiries/{id}")
    public ResponseEntity<ApiResponse<?>> getTargetInquiry(@PathVariable(name = "id") Long inquiryId){
        InquiryInfoResponseDTO responseDTO = inquiryService.getTargetInquiry(inquiryId);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.createSuccess(responseDTO, inquiryId + "번 문의내역 가져오기 성공"));
    }


}
