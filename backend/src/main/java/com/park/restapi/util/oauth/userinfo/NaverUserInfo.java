package com.park.restapi.util.oauth.userinfo;

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
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public RegistrationId getRegistrationId() {
        return registrationId;
    }

    @Override
    public String getEmail() {
        return (String) ((Map) attributes.get("naver_account")).get("email");
    }

    @Override
    public String getNickname() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("naver_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        return (String) profile.get("nickname");
    }
}
