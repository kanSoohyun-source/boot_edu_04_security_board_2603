package boot_edu_04_security_board_2603.security;

import boot_edu_04_security_board_2603.domain.Member;
import boot_edu_04_security_board_2603.domain.MemberRole;
import boot_edu_04_security_board_2603.repository.MemberRepository;
import boot_edu_04_security_board_2603.security.dto.MemberSecurityDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public CustomOAuth2UserService(MemberRepository memberRepository) {
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("=== CustomOAuth2UserService loadUser ===");
        log.info(userRequest);

        // 우선 카카오 로그인 후에 어떤 정보들이 전달되었는지를 확인하도록 수정
        log.info("=== CustomOAuth2UserService Auth2 User ===");
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();

        log.info("name : {}", clientName); // 소셜 서비스 종류 -> naver, google 등
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> paramMap = oAuth2User.getAttributes();

//        paramMap.forEach((key, value) -> {
//            log.info("------------");
//            log.info("key : {}, value : {}", key, value);
//        });
        /*
        key : id, value : 4821276950 -> 식별자 사용이 가능
        key : connected_at, value : 2026-03-30T00:26:59Z
        key : properties, value : {nickname=🐾}
        key : kakao_account,
        value : {profile_nickname_needs_agreement=false,
                profile_image_needs_agreement=true,
                profile={nickname=🐾, is_default_nickname=false},
                has_email=true,
                email_needs_agreement=false,
                is_email_valid=true, 3
                is_email_verified=true,
                email=kkk2186800@naver.com}
         */

        String email = null;
        switch (clientName) {
            case "kakao":
                email = getKakaoEmail(paramMap);
                break;
            case "google":
                email = getGoogleEmail(paramMap);
                break;
        }
        log.info("-------------------------------");
        log.info("email : {}", email);
        log.info("-------------------------------");

        return generateDTO(email, paramMap);
    }

    private String getGoogleEmail(Map<String, Object> paramMap) {
        return null;
    }

    private String getKakaoEmail(Map<String, Object> paramMap) {
        log.info("=== CustomOAuth2UserService getKakaoEmail ===");

        Object value = paramMap.get("kakao_account");
        log.info("kakao_account : {}", value);

        LinkedHashMap accountMap = (LinkedHashMap) value;
        String email = (String) accountMap.get("email");

        log.info("getKakaoEmail email : {}", email);
        return email;
    }

    private MemberSecurityDTO generateDTO(String email, Map<String, Object> paramMap) {
        /*
        카카오 서비스에서 얻어온 이메일을 이용해서 같은 이메일을 가진 사용자를 찾아보고
        없는 경우에는 자동으로 회원가입을 하고 MemberSecurityDTO 를 반환하도록 구성
         */
        log.info("=== CustomOAuth2UserService generateDTO ===");
        Optional<Member> result = memberRepository.findByEmail(email);
        // 1. DB에 해당 이메일의 사용자가 없다면 -> 회원가입 진행
        if (result.isEmpty()) {
            // 회원 추가 - mid 는 이메일 주소, mpw 는 1111
            Member member = Member.builder()
                    .mid(email)
                    .mpw(passwordEncoder.encode("1111"))
                    .email(email) // 이메일 중복 체크를 위함
                    .social(true) // 일반 로그인이 안됨
                    .build();
            member.addRole(MemberRole.USER);
            memberRepository.save(member);

            // MemberSecurityDTO 생성 후 반환
            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                    email, "1111", email, false, true,
                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );
            memberSecurityDTO.setProps(paramMap);
            return memberSecurityDTO;
        } else { // 2. 있다면
            Member member = result.get();
            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                    member.getMid(),
                    member.getMpw(),
                    member.getEmail(),
                    member.isDel(),
                    member.isSocial(),
                    member.getRoleSet().stream()
                            .map(memberRole -> new SimpleGrantedAuthority("ROLE_" + memberRole.name()))
                            .collect(Collectors.toList())
            );
            return memberSecurityDTO;
        }
    }
}
