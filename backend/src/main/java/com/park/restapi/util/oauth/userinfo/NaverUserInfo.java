package com.park.restapi.util.oauth.userinfo;

import com.park.restapi.domain.member.entity.SocialType;
import com.park.restapi.util.oauth.RegistrationId;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo {

    private static final String ID = "id";
    private static final String EMAIL = "email";
    private static final String NICKNAME = "nickname";
    private static final String RESPONSE = "response";

    private Map<String, Object> attributes;
    private RegistrationId registrationId;

    public NaverUserInfo(Map<String, Object> attributes, RegistrationId registrationId) {
        this.attributes = attributes;
        this.registrationId = registrationId;
    }

    @Override
    public String getProviderId() {
        return getResponse(ID);
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
        return getResponse(EMAIL);
    }

    @Override
    public String getNickname() {
        return getResponse(NICKNAME);
    }

    private String getResponse(String target) {
        return String.valueOf(((Map) attributes.get(RESPONSE)).get(target));
    }
}
