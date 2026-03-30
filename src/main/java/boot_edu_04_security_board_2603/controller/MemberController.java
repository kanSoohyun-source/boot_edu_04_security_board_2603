package boot_edu_04_security_board_2603.controller;

import boot_edu_04_security_board_2603.dto.MemberJoinDTO;
import boot_edu_04_security_board_2603.security.dto.MemberSecurityDTO;
import boot_edu_04_security_board_2603.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        boolean isDup = memberService.isDuplicateMid(mid);
        result.put("isDup", isDup);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/join")
    public void join() {
        /* 회원가입 처리 */
        log.info("=== MemberController join get ===");
    }

    @PostMapping("/join")
    public String joinPOST(MemberJoinDTO memberJoinDTO,
                           RedirectAttributes redirectAttributes) {
        /* 회원가입 처리 */
        log.info("=== MemberController joinPOST post ===");
        log.info("memberJoinDTO: {}", memberJoinDTO);

        try {
            memberService.join(memberJoinDTO);
        } catch (MemberService.MidExistException e) {
            // 아이디 중복인 경우
            // MidExistException 발생 시에는 다시 회원 가입 페이지로 이동
            redirectAttributes.addFlashAttribute("error", "mid");
            return "redirect:/member/join";
        }

        return "redirect:/board/list";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/modify")
    public void modify() {
        /* 비밀번호 수정 처리 */
        log.info("=== MemberController modify get ===");
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/modify")
    public String modifyPOST(String mpw,
                             Authentication authentication) {
        /* 회원가입 처리 */
        log.info("=== MemberController modifyPOST post ===");
        log.info("mpw: {}", mpw);

        MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) authentication.getPrincipal();
        String mid = memberSecurityDTO.getMid();
        log.info("mid: {}", mid);

        try {
            memberService.modifyPassword(mid, mpw);
        } catch (MemberService.MidExistException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/board/list";
    }

}
