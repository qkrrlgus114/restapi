package com.park.restapi.util.service;

import com.park.restapi.domain.exception.exception.MemberException;
import com.park.restapi.domain.exception.info.MemberExceptionInfo;
import com.park.restapi.domain.member.entity.Member;
import com.park.restapi.domain.member.repository.MemberRepository;
import com.park.restapi.util.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberFindService {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    // 현재 유저 가져오기
    public Member getCurrentMember() {
        Long currentUserId = jwtService.getCurrentUserId();
        return memberRepository.findById(currentUserId)
                .orElseThrow(() -> new MemberException(MemberExceptionInfo.NOT_FOUND_MEMBER, currentUserId + "번 유저를 찾지 못했습니다."));
    }

    // 유저와 권한 함께 가져오기
    public Member getCurrentMemberFetchJoinRoles() {
        Long currentUserId = jwtService.getCurrentUserId();
        return memberRepository.findByIdFetchRole(currentUserId)
                .orElseThrow(() -> new MemberException(MemberExceptionInfo.NOT_FOUND_MEMBER, currentUserId + "번 유저를 찾지 못했습니다."));
    }

    // 현재 유저 가져오기(null 리턴)
    public Member getCurrentMemberNull() {
        Long currentUserId = jwtService.getCurrentUserId();
        if (currentUserId == null) {
            return null;
        }

        return memberRepository.findById(currentUserId)
                .orElseThrow(() -> new MemberException(MemberExceptionInfo.NOT_FOUND_MEMBER, currentUserId + "번 유저를 찾지 못했습니다."));
    }
}
