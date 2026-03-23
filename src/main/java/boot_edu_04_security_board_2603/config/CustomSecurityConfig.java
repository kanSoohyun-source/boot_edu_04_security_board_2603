package boot_edu_04_security_board_2603.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Log4j2
@Configuration
// @EnableMethodSecurity 의 prePostEnabled 속성은 원하는 곳에
// @PreAuthorize 또는 @PostAuthorize 를 이용해서 시전 또는 사후의 권한은 체크할 수 있음
// 자바 메소드 단위로 세밀하게 권한을 제어할 수 있게 해주는 어노테이션
@EnableMethodSecurity

public class CustomSecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // spring security 의 설정을 담당
        log.info("=== Security Config ===");
        //  spring security 에서 기본드로 제공하는 /login 은 CSS 수정이 불가
        //  => 보통 커스텀 로그인 화면을 따로 생성

        // spring security 에서 form 기반 로그인 설정
        httpSecurity.formLogin(config -> {
            config.loginPage("/member/login"); // 사용자 정의 로그인 페이지 설정
        });

        // CSRF 토큰 사용 안함
        httpSecurity.csrf(csrf -> csrf.disable());
        return httpSecurity.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // spring security 에서 정적 리소스나 보안 필터 제외 대상 설정할 때 사용
        log.info("=== Web configure ===");

        // 정적 파일 경로에 security 적용 안 함
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    // 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        // default password encoder configured
        return new BCryptPasswordEncoder();
    }
}
