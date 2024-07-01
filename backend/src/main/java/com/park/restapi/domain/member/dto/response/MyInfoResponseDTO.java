package com.park.restapi.domain.member.dto.response;

import lombok.Builder;

@Builder
public record MyInfoResponseDTO(
        int totalUseToken,
        int totalAcquisitionToken
) {

    public static MyInfoResponseDTO toDTO(int totalUseToken, int totalAcquisitionToken) {
        return MyInfoResponseDTO.builder()
                .totalAcquisitionToken(totalAcquisitionToken)
                .totalUseToken(totalUseToken)
                .build();
    }
}
