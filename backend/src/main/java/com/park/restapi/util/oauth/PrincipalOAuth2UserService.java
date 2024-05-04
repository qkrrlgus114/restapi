package com.park.restapi.util.oauth;

import com.park.restapi.domain.refreshtoken.entity.RefreshToken;
import com.park.restapi.domain.refreshtoken.repository.RefreshTokenRepository;
import com.park.restapi.domain.member.entity.Role;
import com.park.restapi.domain.member.entity.SocialType;
import com.park.restapi.domain.member.entity.Member;
import com.park.restapi.domain.member.entity.MemberRole;
import com.park.restapi.domain.member.repository.MemberRepository;
import com.park.restapi.domain.member.repository.MemberRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;


/*
 * 해당 서비스 안에 들어오면 유저 접근 권한을 얻은 상태.
 * 해당 유저 여부를 판단하고 없으면 유저 정보 생성
 * */
@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 소셜 타입에 맞게 유동적으로 담을 인터페이스 변수 생성
        OAuth2UserInfo oAuth2UserInfo = null;

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        /*
         * 확장성을 위함.
         * kakao, naver, google 등등 다양한 타입에 맞게 수정만 해주면 됨.
         * */
        if(registrationId.equals("kakao")){
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        }

        String email = oAuth2UserInfo.getEmail();
        String nickname = oAuth2UserInfo.getNickname();

        // 유저가 db에 있는지 판단.
        Optional<Member> byUser = memberRepository.findByEmail(email);

        /*
         * 만약에 유저가 없으면 회원가입 진행
         * 랜덤 닉네임 생성 및 기본 프로필사진 설정
         * */
        if(byUser.isEmpty()){
            Member member = new Member(email, nickname, SocialType.KAKAO);
            Member save = memberRepository.save(member);

            MemberRole memberRole = MemberRole.builder()
                    .member(save)
                    .build();
            memberRoleRepository.save(memberRole);

            RefreshToken refreshToken = RefreshToken.builder()
                    .member(save).build();
            refreshTokenRepository.save(refreshToken);
        }

        return oAuth2User;
    }
}