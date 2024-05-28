package com.park.restapi.domain.member.service.impl;

import com.park.restapi.domain.exception.exception.EmailException;
import com.park.restapi.domain.exception.exception.MemberException;
import com.park.restapi.domain.exception.info.EmailExceptionInfo;
import com.park.restapi.domain.exception.info.MemberExceptionInfo;
import com.park.restapi.domain.member.entity.EmailConfirm;
import com.park.restapi.domain.member.entity.Member;
import com.park.restapi.domain.member.entity.WithdrawalMember;
import com.park.restapi.domain.member.repository.EmailConfirmRepository;
import com.park.restapi.domain.member.repository.MemberRepository;
import com.park.restapi.domain.member.repository.WithdrawalMemberRepository;
import com.park.restapi.domain.member.service.EmailService;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    private final EmailConfirmRepository emailConfirmRepository;
    private final MemberRepository memberRepository;
    private final WithdrawalMemberRepository withdrawalMemberRepository;


    private MimeMessage createMessageChange(String to, String authCode)throws Exception{
        MimeMessage  message = emailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);//보내는 대상
        message.setSubject("(restapi 생성기) 비밀번호 재설정 이메일입니다.");//제목

        String msgg="";
        msgg += "<table style='    width: 100% !important;    background: #ffffff;    margin: 0;    padding: 0;    min-width: 100%;    font-family: 'Malgun Gothic', 'Dotum', 'sans-serif';   '>";
        msgg += "<tr><td style='text-align: center;'>";
        msgg += "<img src='https://damda.s3.ap-northeast-2.amazonaws.com/EMAIL_CONFIRM/EMAIL_CONFIRM.png' alt='header' loading='lazy'>";
        msgg += "</td></tr><tr><td style='text-align: center;'>";
        msgg += "<div style='margin-top: 60px; margin-bottom: 30px;'>";
        msgg += "<h1 style='margin-bottom: 40px;'>계정 인증 안내</h1>";
        msgg += "<p style='margin-top: 0; margin-bottom: 15px; line-height:2;'>";
        msgg += "restapi 생성기 계정 본인 확인 메일입니다.<br>";
        msgg += "아래 인증번호를 입력하시고 본인 인증을 완료해주세요.<br>";
        msgg += "(인증번호는 10분간 유효합니다.)";
        msgg += "</p></div></td></tr><tr><td style='text-align: center;'>";
        msgg += "<div style='margin-bottom: 60px;'>";
        msgg += "<p style='display:inline-block;padding:20px 80px;font-size:16px;font-weight:bold;color:#fff;background:#9a50ff;'>";
        msgg += "인증번호: " + authCode;
        msgg += "</p></div></td></tr><tr><td style='text-align: center;'>";
        msgg += "<img src='https://damda.s3.ap-northeast-2.amazonaws.com/EMAIL_CONFIRM/DAMDA.png' alt='footer' loading='lazy'>";
        msgg += "</td></tr></tbody></table>";
        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("qkrrlgus9797@gmail.com","restapi 생성기"));//보내는 사람

        return message;
    }

    private MimeMessage createMessageRegist(String to, String authCode)throws Exception{
        MimeMessage  message = emailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);//보내는 대상
        message.setSubject("(restapi 생성기) 회원가입 인증번호 이메일입니다.");//제목

        String msgg="";
        msgg += "<table style='    width: 100% !important;    background: #ffffff;    margin: 0;    padding: 0;    min-width: 100%;    font-family: 'Malgun Gothic', 'Dotum', 'sans-serif';   '>";
        msgg += "<tr><td style='text-align: center;'>";
        msgg += "<img src='https://damda.s3.ap-northeast-2.amazonaws.com/EMAIL_CONFIRM/EMAIL_CONFIRM.png' alt='header' loading='lazy'>";
        msgg += "</td></tr><tr><td style='text-align: center;'>";
        msgg += "<div style='margin-top: 60px; margin-bottom: 30px;'>";
        msgg += "<h1 style='margin-bottom: 40px;'>계정 인증 안내</h1>";
        msgg += "<p style='margin-top: 0; margin-bottom: 15px; line-height:2;'>";
        msgg += "restapi 생성기 계정 본인 확인 메일입니다.<br>";
        msgg += "아래 인증번호를 입력하시고 본인 인증을 완료해주세요.<br>";
        msgg += "(인증번호는 10분간 유효합니다.)";
        msgg += "</p></div></td></tr><tr><td style='text-align: center;'>";
        msgg += "<div style='margin-bottom: 60px;'>";
        msgg += "<p style='display:inline-block;padding:20px 80px;font-size:16px;font-weight:bold;color:#fff;background:#9a50ff;'>";
        msgg += "인증번호: " + authCode;
        msgg += "</p></div></td></tr><tr><td style='text-align: center;'>";
        msgg += "<img src='https://damda.s3.ap-northeast-2.amazonaws.com/EMAIL_CONFIRM/DAMDA.png' alt='footer' loading='lazy'>";
        msgg += "</td></tr></tbody></table>";
        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("qkrrlgus9797@gmail.com","restapi 생성기"));//보내는 사람

        return message;
    }

    public String createKey() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        int length =10;
        SecureRandom rnd = new SecureRandom();

        StringBuilder key = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            key.append(characters.charAt(rnd.nextInt(characters.length())));
        }

        return key.toString();
    }

    public String sendSimpleMessageChange(String to)throws Exception {
        String authCode =  createKey(); // 인증코드 생성
        MimeMessage message = createMessageChange(to, authCode); // 메시지 생성
        try{ // 예외처리
            emailSender.send(message);
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return authCode;
    }

    @Transactional
    public boolean sendSimpleMessageRegist(String email)throws Exception {

        Optional<WithdrawalMember> withdrawalMemberRepositoryByEmail = withdrawalMemberRepository.findByEmail(email);
        if (withdrawalMemberRepositoryByEmail.isPresent()) {
            throw new MemberException(MemberExceptionInfo.WITHDRAWAL_MEMBER, email + "로 회원가입을 시도했습니다.(탈퇴 유저)");
        }

        Optional<Member> byEmail = memberRepository.findByEmail(email);
        if(byEmail.isPresent()) throw new EmailException(EmailExceptionInfo.ALREADY_SIGN_UP_EMAIL, "이미 가입된 이메일입니다.");

        String authCode =  createKey(); // 인증코드 생성
        MimeMessage message = createMessageRegist(email, authCode); // 메시지 생성

        EmailConfirm confirm = EmailConfirm.builder()
                .certificationNumber(authCode)
                .certificationStatus(false).build();
        emailConfirmRepository.save(confirm);

        try{ // 예외처리
            emailSender.send(message);
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return true;
    }

    @Override
    @Transactional
    public void checkCertificationCode(String code) {
        EmailConfirm confirm = emailConfirmRepository.checkCode(code)
                .orElseThrow(() -> new EmailException(EmailExceptionInfo.NO_MATCH_CERTIFICATION_CODE, "인증번호 일치하지 않음"));

        LocalDateTime createDate = confirm.getCreateDate();
        LocalDateTime now = LocalDateTime.now();

        Duration duration = Duration.between(createDate, now);
        if(duration.toMinutes() > 3){
            throw new EmailException(EmailExceptionInfo.ALREADY_EXPIRED_CERTIFICATION_CODE, "인증번호 만료");
        }

        // 사용 체크
        confirm.changedUsing();
    }

}
