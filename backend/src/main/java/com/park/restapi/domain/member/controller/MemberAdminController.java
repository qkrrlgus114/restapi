package com.park.restapi.domain.member.controller;

import com.park.restapi.domain.member.service.MemberService;
import com.park.restapi.util.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/api/admin/members")
public class MemberAdminController {

    private final MemberService memberService;

    // 유저 추방
    @PatchMapping("/{id}/ban")
    public ResponseEntity<ApiResponse<Void>> bannedMember(@PathVariable(name = "id") Long id) {
        memberService.bannedMember(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
