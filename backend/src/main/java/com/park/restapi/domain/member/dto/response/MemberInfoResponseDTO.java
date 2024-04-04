package com.park.restapi.domain.member.dto.response;

import com.park.restapi.domain.coupon.entity.Coupon;
import com.park.restapi.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberInfoResponseDTO {
    private String nickname;
    private int token;
    private int remainingQuantity;

    @Builder
    public MemberInfoResponseDTO(String nickname, int token, int remainingQuantity) {
        this.nickname = nickname;
        this.token = token;
        this.remainingQuantity = remainingQuantity;
    }


    public static MemberInfoResponseDTO fromEntity(Member member, Coupon coupon){
        return MemberInfoResponseDTO.builder()
                .nickname(member.getNickname())
                .token(member.getToken())
                .remainingQuantity(coupon.getRemainingQuantity()).build();
    }
}
