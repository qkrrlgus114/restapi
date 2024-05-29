package com.park.restapi.domain.member.dto.response;

import com.park.restapi.domain.member.entity.Member;
import com.park.restapi.domain.member.entity.SocialType;
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

    private boolean social;

    @Builder
    public MemberInfoResponseDTO(String nickname, int token, List<String> memberRoles, boolean social) {
        this.nickname = nickname;
        this.token = token;
        this.memberRoles = memberRoles;
        this.social = social;
    }

    public static MemberInfoResponseDTO toDTO(Member member){

        boolean social = member.getSocialType().equals(SocialType.KAKAO) ? true : false;

        List<String> roles = member.getMemberRoles().stream()
                .map(memberRole -> memberRole.getRole().name())
                .collect(Collectors.toList());

        return MemberInfoResponseDTO.builder()
                .nickname(member.getNickname())
                .token(member.getToken())
                .social(social)
                .memberRoles(roles).build();
    }
}
