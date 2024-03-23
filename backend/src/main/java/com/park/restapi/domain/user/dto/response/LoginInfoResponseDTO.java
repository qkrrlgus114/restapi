package com.park.restapi.domain.user.dto.response;

import com.park.restapi.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginInfoResponseDTO {
    private String nickname;
    private int token;

    @Builder
    public LoginInfoResponseDTO(String nickname, int token) {
        this.nickname = nickname;
        this.token = token;
    }

    public static LoginInfoResponseDTO fromEntity(User user){
        return LoginInfoResponseDTO.builder()
                .nickname(user.getNickname())
                .token(user.getToken()).build();
    }
}
