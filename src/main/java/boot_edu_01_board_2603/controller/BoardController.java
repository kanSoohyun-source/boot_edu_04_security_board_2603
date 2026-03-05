package boot_edu_01_board_2603.controller;

import boot_edu_01_board_2603.dto.BoardDTO;
import boot_edu_01_board_2603.dto.PageRequestDTO;
import boot_edu_01_board_2603.dto.PageResponseDTO;
import boot_edu_01_board_2603.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Log4j2
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {
        log.info("list get...");

        PageResponseDTO<BoardDTO> pageResponseDTO = boardService.getList(pageRequestDTO);
        log.info("pageResponseDTO: {}", pageResponseDTO);

        model.addAttribute("pageResponseDTO", pageResponseDTO);
    }

    // 등록 /board/add
    @GetMapping("/add")
    public void add() {
        log.info("add get...");

    }

    @PostMapping("/add")
    public String add(@Valid BoardDTO boardDTO, BindingResult bindingResult,
                    RedirectAttributes redirectAttributes) {
        log.info("add post...");

        // 1. 유효성 검사에서 문제가 있는 경우
        if (bindingResult.hasErrors()) {
            log.info(bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/board/add";
        }

        // 2. 문제가 없는 경우, 등록 작업
        log.info("boardDTO: {}", boardDTO);
        Long bno = boardService.add(boardDTO);
        redirectAttributes.addFlashAttribute("result", bno); // 세션에 값 저장
        return "redirect:/board/list";
    }

    // 조회
    @GetMapping("/read")
    public void read(Long bno, PageRequestDTO pageRequestDTO, Model model) {
        log.info("read get...");

        BoardDTO boardDTO = boardService.readOne(bno);
        log.info("boardDTO: {}", boardDTO);
        model.addAttribute("boardDTO", boardDTO);
    }
}