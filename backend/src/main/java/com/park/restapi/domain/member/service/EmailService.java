package com.park.restapi.domain.member.service;

public interface EmailService {

//    Long changeTempKey(String key, String email, User user);
//
//    Long registTempKey(String key, String email);

    String sendSimpleMessageChange(String email)throws Exception;

    boolean sendSimpleMessageRegist(String email)throws Exception;

    // 인증번호 확인 및 인증
    void checkCertificationCode(String code);

}
