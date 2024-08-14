package com.park.restapi.util.oauth;

import com.park.restapi.util.oauth.userinfo.KakaoUserInfo;
import com.park.restapi.util.oauth.userinfo.NaverUserInfo;
import com.park.restapi.util.oauth.userinfo.OAuth2UserInfo;

import java.util.Map;

public enum RegistrationId {
    KAKAO {
        @Override
        public OAuth2UserInfo getOAuth2UserInfo(Map<String, Object> attributes, RegistrationId registrationId) {
            return new KakaoUserInfo(attributes, registrationId);
        }
    },
    NAVER {
        @Override
        public OAuth2UserInfo getOAuth2UserInfo(Map<String, Object> attributes, RegistrationId registrationId) {
            return new NaverUserInfo(attributes, registrationId);
        }
    };

    public abstract OAuth2UserInfo getOAuth2UserInfo(Map<String, Object> attributes, RegistrationId registrationId);
}
