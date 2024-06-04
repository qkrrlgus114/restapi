package com.park.restapi.util.oauth;

/*
 * 다른 oauth에서 유저 정보를 가져오기 위한 메서드를 정의해놓은 인터페이스.
 * 필요한 내용은 추가해서 공통으로 사용하면 됩니다.
 * */
public interface OAuth2UserInfo {
    String getProviderId();

    String getProvider();

    String getEmail();

    String getNickname();
}