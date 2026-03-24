package boot_edu_04_security_board_2603.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequestMapping("/member")
public class MemberController {

    @GetMapping("/login")
    public void login(String error, String logout) {
        log.info("=== MemberController login ===");
        log.info("logout: {}", logout);

        if (logout != null) { // ?logout 형식으로 주소가 들어온 경우
            log.info("=== User Logout ===");
        }
    }
}
