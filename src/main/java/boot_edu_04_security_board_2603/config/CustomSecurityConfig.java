package boot_edu_04_security_board_2603.config;

import boot_edu_04_security_board_2603.security.CustomUserDetailsService;
import boot_edu_04_security_board_2603.security.handler.Custom403Handler;
import boot_edu_04_security_board_2603.security.handler.CustomSocialLoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Log4j2
@Configuration
// @EnableMethodSecurity 의 prePostEnabled 속성은 원하는 곳에
// @PreAuthorize 또는 @PostAuthorize 를 이용해서 시전 또는 사후의 권한은 체크할 수 있음
// 자바 메소드 단위로 세밀하게 권한을 제어할 수 있게 해주는 어노테이션
@EnableMethodSecurity
@RequiredArgsConstructor
public class CustomSecurityConfig {
    // Bean 주입 필요
    private final DataSource dataSource; // 데이터베이스 이용 때문에 추가
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // spring security 의 설정을 담당
        log.info("=== Security Config ===");
        //  spring security 에서 기본으로 제공하는 /login 은 CSS 수정이 불가
        //  => 보통 커스텀 로그인 화면을 따로 생성

        // spring security 에서 form 기반 로그인 설정
        httpSecurity.formLogin(config -> {
            config.loginPage("/member/login"); // 사용자 정의 로그인 페이지 설정
            config.successHandler(authenticationSuccessHandler());
        });

        // CSRF 토큰 사용 안함
        httpSecurity.csrf(csrf -> csrf.disable());

        // remember-me 설정
        httpSecurity.rememberMe(rememberMe -> {
           rememberMe.key("12345678") // 토큰 암호화에 사용할 키
            .tokenRepository(persistentTokenRepository()) // 토큰 저장소를 지정 => DB
            .userDetailsService(customUserDetailsService) // 인증 처리에 사용할 사용자 정보를 담을 서비스
            .tokenValiditySeconds(60 * 60 * 24 * 60); // 60일 동안 유효
        });

        httpSecurity.exceptionHandling(config -> {
            config.accessDeniedHandler(accessDeniedHandler());
        });
        // sns 로그인
        httpSecurity.oauth2Login(config -> {
           config.loginPage("/member/login");
        });

        httpSecurity.oauth2Login(config -> {
            config.loginPage(("/member/login"));
            config.successHandler(authenticationSuccessHandler());
        });

        return httpSecurity.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new Custom403Handler();
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

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        /*
        Spring Security 에서 remember-me 기능을 위한 Persistent Token 저장소 설정
        -> 프로젝트에 따라
          => Persistent Token 저장소를 DB 에도 저장 가능
          => DB 라 여러 개인 경우도 있음
         */
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setDataSource(dataSource);
        return repository;
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomSocialLoginSuccessHandler();
    }

}
