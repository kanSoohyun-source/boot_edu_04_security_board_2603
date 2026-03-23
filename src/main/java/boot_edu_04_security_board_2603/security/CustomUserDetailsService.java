package boot_edu_04_security_board_2603.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("=== CustomUserDetailsService loadUserByUsername ===");
        // Spring Security 가 로그인 시 사용자 인증을 위해 호출하는 메서드
        log.info("loadUserByUsername: {}", username);

        // UserDetails 는 사용자 인증(Authentication)과 관련된 정보를 저장하는 역할
        // Spring security 는 내부적으로 UserDetails 타입의 객체를 이용해서 패스워드를 검사하고
        // 사용자 권한을 확인하는 방법으로 동작

        // 여기서 사용자 정보를 DB 에서 조회하여 UserDetails 객체를 반환해야 함
        UserDetails userDetails = User.builder()
                .username("user1")
                .password(passwordEncoder.encode("1111")) // 비밀번호는 반드시 암호화되어야 함
                .authorities("ROLE_USER")
                .build();

        return userDetails;
    }
}
