package com.park.restapi.util.oauth.userinfo;

import com.park.restapi.domain.member.entity.SocialType;
import com.park.restapi.util.oauth.RegistrationId;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo {

    private Map<String, Object> attributes;
    private RegistrationId registrationId;

    public NaverUserInfo(Map<String, Object> attributes, RegistrationId registrationId) {
        this.attributes = attributes;
        this.registrationId = registrationId;
    }

    @Override
    public String getProviderId() {
        return String.valueOf(((Map) attributes.get("response")).get("id"));
    }

    @Override
    public SocialType getProvider() {
        return SocialType.NAVER;
    }

    @Override
    public RegistrationId getRegistrationId() {
        return registrationId;
    }

    @Override
    public String getEmail() {
        return String.valueOf(((Map) attributes.get("response")).get("email"));
    }

    @Override
    public String getNickname() {
        return String.valueOf(((Map) attributes.get("response")).get("nickname"));
    }
}
