package com.park.restapi.domain.member.dto.response;

import com.park.restapi.domain.coupon.entity.Coupon;
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
    private int remainingQuantity;
    private List<String> memberRoles;

    @Builder
    public MemberInfoResponseDTO(String nickname, int token, int remainingQuantity, List<String> memberRoles) {
        this.nickname = nickname;
        this.token = token;
        this.remainingQuantity = remainingQuantity;
        this.memberRoles = memberRoles;
    }

    public static MemberInfoResponseDTO fromEntity(Member member, Coupon coupon){

        List<String> roles = member.getMemberRoles().stream()
                .map(memberRole -> memberRole.getRole().name())
                .collect(Collectors.toList());

        int remainingQuantity = (coupon != null) ? coupon.getRemainingQuantity() : 0;

        return MemberInfoResponseDTO.builder()
                .nickname(member.getNickname())
                .token(member.getToken())
                .remainingQuantity(remainingQuantity)
                .memberRoles(roles).build();
    }
}
