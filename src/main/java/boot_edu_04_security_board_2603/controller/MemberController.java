package boot_edu_04_security_board_2603.controller;

import boot_edu_04_security_board_2603.dto.MemberJoinDTO;
import boot_edu_04_security_board_2603.repository.MemberRepository;
import boot_edu_04_security_board_2603.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/login")
    public void login(String error, String logout) {
        log.info("=== MemberController login ===");
        log.info("logout: {}", logout);

        if (logout != null) { // ?logout 형식으로 주소가 들어온 경우
            log.info("=== User Logout ===");
        }
    }

    @GetMapping("/isDupId")
    public ResponseEntity<Map<String, Boolean>> isDupId(String mid) {
        /* 아이디 체크를 위한 API */
        Map<String, Boolean> result = new HashMap<>();
        boolean isDup = memberService.checkMid(mid);
        result.put("isDup", isDup);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/join")
    public void join() {
        /* 회원가입 처리 */
        log.info("=== MemberController join get ===");
    }

    @PostMapping("/join")
    public String joinPOST(MemberJoinDTO memberJoinDTO) {
        /* 회원가입 처리 */
        log.info("=== MemberController joinPOST post ===");
        log.info("memberJoinDTO: {}", memberJoinDTO);

        return "redirect:/board/list";
    }
}
