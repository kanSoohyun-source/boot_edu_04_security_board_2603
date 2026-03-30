package boot_edu_04_security_board_2603.security.handler;

import boot_edu_04_security_board_2603.security.dto.MemberSecurityDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Log4j2
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {
    private final PasswordEncoder passwordEncoder;

    public CustomSocialLoginSuccessHandler() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // 소셜 로그인이 성공한 경우에 자동으로 실행
        // 회원의 Password 가 "1111"인 경우 정보수정 페이지로 이동
        log.info("=== CustomSocialLoginSuccessHandler onAuthenticationSuccess ===");
        log.info(authentication.getPrincipal()); // 인증된 회원 정보 가져오기

        MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) authentication.getPrincipal();
        // 암호화된 비밀번호가 저장되어 있음
        String encPassword = memberSecurityDTO.getMpw();
        log.info("encPassword : {}",encPassword);

        // 소셜 로그인이고 비밀번호가 "1111"이면
        if (memberSecurityDTO.isSocial() && passwordEncoder.matches("1111", encPassword)) {
            log.info("social login success");

            log.info("Redirect to Member Modify");
            response.sendRedirect("/member/modify");

            return;
        } else {
            response.sendRedirect("/board/list");
        }
    }
}
