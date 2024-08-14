package com.park.restapi.domain.member.dto.response;

import com.park.restapi.domain.member.entity.Member;
import com.park.restapi.domain.member.entity.SocialType;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record MemberInfoResponseDTO(
        String nickname,
        int token,
        List<String> memberRoles,
        boolean social
) {

    public static MemberInfoResponseDTO toDTO(Member member) {
        boolean social = member.getSocialType().equals(SocialType.GENERAL) ? false : true;

        List<String> roles = member.getMemberRoles().stream()
                .map(memberRole -> memberRole.getRole().name())
                .collect(Collectors.toList());

        return MemberInfoResponseDTO.builder()
                .nickname(member.getNickname())
                .token(member.getToken())
                .social(social)
                .memberRoles(roles)
                .build();
    }
}
