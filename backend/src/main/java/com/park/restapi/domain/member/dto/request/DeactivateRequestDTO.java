package com.park.restapi.domain.member.dto.request;

import com.park.restapi.domain.member.entity.SocialType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeactivateRequestDTO {

    @NotNull
    private SocialType socialType;
    private String password;

    public DeactivateRequestDTO(@NotNull SocialType socialType, String password) {
        this.socialType = socialType;
        this.password = password;
    }

    public DeactivateRequestDTO(@NotNull SocialType socialType) {
        this.socialType = socialType;
    }
}
