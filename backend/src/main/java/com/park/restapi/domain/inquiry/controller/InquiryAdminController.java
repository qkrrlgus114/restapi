package com.park.restapi.domain.inquiry.controller;

import com.park.restapi.domain.inquiry.dto.response.InquiryListResponseDTO;
import com.park.restapi.domain.inquiry.service.InquiryService;
import com.park.restapi.util.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/inquiries")
@RequiredArgsConstructor
public class InquiryAdminController {

    private final InquiryService inquiryService;

    // 현재 유저의 문의 내역 가져오기
    @GetMapping()
    public ResponseEntity<ApiResponse<InquiryListResponseDTO>> getInquiries(@RequestParam(value = "page", defaultValue = "1") int page) {
        InquiryListResponseDTO myInquiries = inquiryService.getMyInquiries(page - 1);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.createSuccess(myInquiries, "내 문의내역 가져오기 성공"));
    }

}
