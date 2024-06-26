package com.park.restapi.domain.member.controller;

import com.park.restapi.domain.member.dto.request.DeactivateRequestDTO;
import com.park.restapi.domain.member.dto.request.LoginInfoRequestDTO;
import com.park.restapi.domain.member.dto.request.SignUpRequestDTO;
import com.park.restapi.domain.member.dto.response.MemberInfoResponseDTO;
import com.park.restapi.domain.member.dto.response.MyInfoResponseDTO;
import com.park.restapi.domain.member.entity.SocialType;
import com.park.restapi.domain.member.service.MemberService;
import com.park.restapi.util.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping()
    public ResponseEntity<ApiResponse<Void>> signUp(@Valid @RequestBody SignUpRequestDTO signUpRequestDTO) throws IOException, InterruptedException {
        memberService.signUp(signUpRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Void>> login(@Valid @RequestBody LoginInfoRequestDTO loginInfoRequestDTO,
                                                   HttpServletResponse response) {
        memberService.login(loginInfoRequestDTO, response);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 소셜로그인
    @PostMapping("/social-login")
    public ResponseEntity<ApiResponse<Void>> socialLogin(HttpServletResponse response) {
        memberService.socialLogin(response);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 토큰 조회
    @GetMapping("/tokens")
    public ResponseEntity<ApiResponse<Integer>> getToken() {
        int token = memberService.getToken();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccess(token, "토큰 개수 조회 성공"));
    }

    // 로그아웃
    @GetMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse response) {
        memberService.logout(response);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 메인화면 유저 정보 제공
    @GetMapping("/main-info")
    public ResponseEntity<ApiResponse<MemberInfoResponseDTO>> getMemberInfo() {
        MemberInfoResponseDTO userInfo = memberService.getUserInfo();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccess(userInfo, "현재 로그인 유저의 정보"));
    }

    // 유저 탈퇴
    @PatchMapping("/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivateMember(@RequestBody @Valid DeactivateRequestDTO deactivateRequestDTO) {
        SocialType socialType = deactivateRequestDTO.socialType();

        switch (socialType) {
            case KAKAO -> memberService.deactivateSocialMember();
            case GENERAL -> memberService.deactivateGeneralMember(deactivateRequestDTO);
            default -> throw new IllegalArgumentException("9000 타입 에러 발생. 관리자에게 문의가 필요합니다.");
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }


    // 유저 개인 정보 제공
    @GetMapping("/details")
    public ResponseEntity<ApiResponse<MyInfoResponseDTO>> getMemberDeepInfo() {
        MyInfoResponseDTO memberInfo = memberService.getMemberInfo();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccess(memberInfo, "회원 개인정보 조회 성공."));
    }
}
