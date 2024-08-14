package com.park.restapi.util.oauth;

import com.park.restapi.domain.member.entity.Member;
import com.park.restapi.domain.member.entity.MemberRole;
import com.park.restapi.domain.member.entity.SocialType;
import com.park.restapi.domain.member.entity.WithdrawalMember;
import com.park.restapi.domain.member.repository.MemberRepository;
import com.park.restapi.domain.member.repository.MemberRoleRepository;
import com.park.restapi.domain.member.repository.WithdrawalMemberRepository;
import com.park.restapi.util.oauth.userinfo.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final WithdrawalMemberRepository withdrawalMemberRepository;

    private static final String KICKED_MEMBER_ERROR = "kicked_member";
    private static final String WITHDRAWAL_MEMBER_ERROR = "withdrawal_member";
    private static final String INVALID_REGISTRATION_ID_ERROR = "invalid_registration_id";
    private static final String WITHDRAWAL_EMAIL_ERROR = "withdrawal_email";
    private static final String DIFF_SOCIAL_TYPE = "diff_social_type";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2UserInfo oAuth2UserInfo = createOAuth2UserInfo(userRequest, oAuth2User);

        String email = oAuth2UserInfo.getEmail();
        checkBlackList(email); // 블랙리스트 확인

        // 유저가 db에 있는지 판단.
        Optional<Member> byUser = memberRepository.findByEmail(email);

        /*
         * 만약에 유저가 없으면 회원가입 진행
         * */
        if (byUser.isEmpty()) {
            registerNewMember(oAuth2UserInfo, email);
        } else {
            checkExistingMember(byUser.get(), oAuth2UserInfo);
        }

        return oAuth2User;
    }

    // OAuth2UserInfo 생성
    private OAuth2UserInfo createOAuth2UserInfo(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId().toUpperCase();
        try {
            RegistrationId regId = RegistrationId.valueOf(registrationId);
            return regId.getOAuth2UserInfo(oAuth2User.getAttributes(), regId);
        } catch (IllegalArgumentException e) {
            throw new OAuth2AuthenticationException(new OAuth2Error(INVALID_REGISTRATION_ID_ERROR, "지원하지 않는 registration id: " + registrationId, null));
        }
    }

    // 블랙리스트에 있는지 확인
    private void checkBlackList(String email) {
        Optional<WithdrawalMember> byEmail = withdrawalMemberRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            OAuth2Error error = new OAuth2Error(WITHDRAWAL_EMAIL_ERROR, email + " 유저가 로그인 시도를 진행했습니다.(탈퇴한 유저)", null);
            throw new OAuth2AuthenticationException(error);
        }
    }

    // 새로운 유저 가입
    private void registerNewMember(OAuth2UserInfo oAuth2UserInfo, String email) {
        String nickname = oAuth2UserInfo.getNickname();
        Member member = new Member(email, nickname, SocialType.valueOf(oAuth2UserInfo.getRegistrationId().name()));
        Member savedMember = memberRepository.save(member);

        MemberRole memberRole = MemberRole.builder()
                .member(savedMember)
                .build();
        memberRoleRepository.save(memberRole);
    }

    // 기존 유저 판단
    private void checkExistingMember(Member member, OAuth2UserInfo oAuth2UserInfo) {
        // 동일한 소셜인지 판단
        if (!member.getSocialType().equals(oAuth2UserInfo.getProvider())) {
            OAuth2Error error = new OAuth2Error(DIFF_SOCIAL_TYPE, member.getEmail() + " 유저가 로그인 시도를 진행했습니다.(다른 소셜 타입)",
                    null);
            throw new OAuth2AuthenticationException(error);
        }
        // 추방 여부 판단
        if (member.getBannedDate() != null) {
            OAuth2Error error = new OAuth2Error(KICKED_MEMBER_ERROR, member.getEmail() + " 유저가 로그인 시도를 진행했습니다.(추방된 유저)",
                    null);
            throw new OAuth2AuthenticationException(error);
        }

        // 탈퇴 여부 판단
        if (member.getWithdrawalDate() != null) {
            OAuth2Error error = new OAuth2Error(WITHDRAWAL_MEMBER_ERROR, member.getEmail() + " 유저가 로그인 시도를 진행했습니다.(탈퇴한 유저)",
                    null);
            throw new OAuth2AuthenticationException(error);
        }
    }
}