package com.park.restapi.util.oauth.userinfo;

import com.park.restapi.util.oauth.RegistrationId;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {

    private Map<String, Object> attributes;
    private RegistrationId registrationId;

    public KakaoUserInfo(Map<String, Object> attributes, RegistrationId registrationId) {
        this.attributes = attributes;
        this.registrationId = registrationId;
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public RegistrationId getRegistrationId() {
        return registrationId;
    }

    @Override
    public String getEmail() {
        return (String) ((Map) attributes.get("kakao_account")).get("email");
    }

    @Override
    public String getNickname() {
        return (String) ((Map) ((Map) attributes.get("kakao_account")).get("profile")).get("nickname");
    }

}