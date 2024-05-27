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
    private int totalUseToken;
    private int totalAcquisitionToken;

    @Builder
    private MyInfoResponseDTO(int totalUseToken, int totalAcquisitionToken) {
        this.totalUseToken = totalUseToken;
        this.totalAcquisitionToken = totalAcquisitionToken;
    }

    public static MyInfoResponseDTO toDTO(int totalUseToken, int totalAcquisitionToken){
        return MyInfoResponseDTO.builder()
                .totalAcquisitionToken(totalAcquisitionToken)
                .totalUseToken(totalUseToken).build();
    }


}
