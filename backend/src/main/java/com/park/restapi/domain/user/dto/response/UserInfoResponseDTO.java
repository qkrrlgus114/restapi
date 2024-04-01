package com.park.restapi.domain.user.dto.response;

import com.park.restapi.domain.coupon.entity.Coupon;
import com.park.restapi.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserInfoResponseDTO {
    private String nickname;
    private int token;
    private int remainingQuantity;

    @Builder
    public UserInfoResponseDTO(String nickname, int token, int remainingQuantity) {
        this.nickname = nickname;
        this.token = token;
        this.remainingQuantity = remainingQuantity;
    }


    public static UserInfoResponseDTO fromEntity(User user, Coupon coupon){
        return UserInfoResponseDTO.builder()
                .nickname(user.getNickname())
                .token(user.getToken())
                .remainingQuantity(coupon.getRemainingQuantity()).build();
    }
}
