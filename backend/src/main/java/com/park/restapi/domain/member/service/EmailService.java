package com.park.restapi.domain.member.service;

public interface EmailService {

    boolean sendSimpleMessageRegist(String email)throws Exception;

    // 인증번호 확인 및 인증
    void checkCertificationCode(String code);

}
