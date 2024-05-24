package com.park.restapi.domain.member.dto.response;

import com.park.restapi.domain.member.entity.Member;
import com.park.restapi.domain.member.entity.SocialType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyInfoResponseDTO {
    private boolean socialMember;
    private String nickname;
    private int totalUseToken;
    private int totalAcquisitionToken;

    @Builder
    private MyInfoResponseDTO(boolean socialMember, String nickname, int totalUseToken, int totalAcquisitionToken) {
        this.socialMember = socialMember;
        this.nickname = nickname;
        this.totalUseToken = totalUseToken;
        this.totalAcquisitionToken = totalAcquisitionToken;
    }

    public static MyInfoResponseDTO toDTO(Member member, int totalUseToken, int totalAcquisitionToken){
        boolean social = member.getSocialType().equals(SocialType.GENERAL) ? false : true;

        return MyInfoResponseDTO.builder()
                .nickname(member.getNickname())
                .socialMember(social)
                .totalAcquisitionToken(totalAcquisitionToken)
                .totalUseToken(totalUseToken).build();
    }


}
