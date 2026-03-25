package boot_edu_04_security_board_2603.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Log4j2
public class Custom403Handler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("=== Custom403Handler handle ===");
        boolean isJsonRequest = false;

        response.setStatus(HttpStatus.FORBIDDEN.value()); // 응답 코드에 403

        // JSON(ajax) 요청이었는지 확인
        String contentType = request.getHeader("Content-Type");
        if (contentType != null) {
            isJsonRequest = contentType.contains("application/json");
        }
        log.info("isJsonRequest: {}", isJsonRequest);

        // 일반 request
        // <form> 방식으로 데이터가 처리되는 경우 로그인 페이지로 리다이렉트
        if (!isJsonRequest) {
            // 로그인 페이지로 이동
            response.sendRedirect("/member/login?error=ACCESS_DENIED");
        }
    }
}
