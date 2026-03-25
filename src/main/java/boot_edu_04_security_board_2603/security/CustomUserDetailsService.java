package boot_edu_04_security_board_2603.security;

import boot_edu_04_security_board_2603.domain.Member;
import boot_edu_04_security_board_2603.repository.MemberRepository;
import boot_edu_04_security_board_2603.security.dto.MemberSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service

public class CustomUserDetailsService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("=== CustomUserDetailsService loadUserByUsername ===");
        // Spring Security 가 로그인 시 사용자 인증을 위해 호출하는 메서드
        // 엔티티를 구현 후 MemberSecurityDTO 구성
        // 반환형이 UserDetails, MemberSecurityDTO 가 User(UserDetails) 를 상속 받아서 반환 시 사용 가능
        log.info("loadUserByUsername: {}", username);

        // UserDetails 는 사용자 인증(Authentication)과 관련된 정보를 저장하는 역할
        // Spring security 는 내부적으로 UserDetails 타입의 객체를 이용해서 패스워드를 검사하고
        // 사용자 권한을 확인하는 방법으로 동작

        // 여기서 사용자 정보를 DB 에서 조회하여 UserDetails 객체를 반환해야 함

        // 1. DB 에서 회원 정보를 들고 옴
        Optional<Member> result = memberRepository.getWithRoles(username);

        // 2. 아이디에 해당하는 회원 정보가 없는 경우
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("회원 정보가 없음");
        }

        // 3. 정보가 있는 경우 반환할 DTO 를 생성
        Member member = result.get();
        MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                member.getMid(),
                member.getMpw(),
                member.getEmail(),
                false,
                false, member.getRoleSet().stream()
                .map(memberRole -> new SimpleGrantedAuthority("ROLE_" + memberRole.name()))
                .collect(Collectors.toList())
        );


        log.info("memberSecurityDTO: {}", memberSecurityDTO);
        return memberSecurityDTO;
    }
}
