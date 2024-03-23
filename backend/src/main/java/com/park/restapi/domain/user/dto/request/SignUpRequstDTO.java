package com.park.restapi.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequstDTO {

    private String email;
    private String password;
    private String nickname;

}
