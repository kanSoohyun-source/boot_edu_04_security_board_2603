package boot_edu_04_security_board_2603.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class RootController {
    @GetMapping("/")
    public String root() {
        return "redirect:/board/list";
    }
}