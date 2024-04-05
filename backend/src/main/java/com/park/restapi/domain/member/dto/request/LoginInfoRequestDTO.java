package com.park.restapi.domain.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginInfoRequestDTO {
    private String email;
    private String password;
    private boolean social = false;
}
