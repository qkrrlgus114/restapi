package com.park.restapi.domain.member.dto.response;

import com.park.restapi.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class MemberInfoResponseDTO {
    private String nickname;
    private int token;
    private List<String> memberRoles;

    @Builder
    public MemberInfoResponseDTO(String nickname, int token, List<String> memberRoles) {
        this.nickname = nickname;
        this.token = token;
        this.memberRoles = memberRoles;
    }

    public static MemberInfoResponseDTO toDTO(Member member){

        List<String> roles = member.getMemberRoles().stream()
                .map(memberRole -> memberRole.getRole().name())
                .collect(Collectors.toList());

        return MemberInfoResponseDTO.builder()
                .nickname(member.getNickname())
                .token(member.getToken())
                .memberRoles(roles).build();
    }
}
