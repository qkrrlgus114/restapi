package com.park.restapi.util.oauth.userinfo;

import com.park.restapi.domain.member.entity.SocialType;
import com.park.restapi.util.oauth.RegistrationId;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {

    private static final String ID = "id";
    private static final String EMAIL = "email";
    private static final String NICKNAME = "nickname";
    private static final String PROFILE = "profile";
    private static final String KAKAO_ACCOUNT = "kakao_account";

    private Map<String, Object> attributes;
    private RegistrationId registrationId;

    public KakaoUserInfo(Map<String, Object> attributes, RegistrationId registrationId) {
        this.attributes = attributes;
        this.registrationId = registrationId;
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get(ID));
    }

    @Override
    public SocialType getProvider() {
        return SocialType.KAKAO;
    }

    @Override
    public RegistrationId getRegistrationId() {
        return registrationId;
    }

    @Override
    public String getEmail() {
        return String.valueOf(getKakaoAccount().get(EMAIL));
    }

    @Override
    public String getNickname() {
        return (String) ((Map) (getKakaoAccount()).get(PROFILE)).get(NICKNAME);
    }

    private Map<String, Object> getKakaoAccount() {
        return (Map) attributes.get(KAKAO_ACCOUNT);
    }

}