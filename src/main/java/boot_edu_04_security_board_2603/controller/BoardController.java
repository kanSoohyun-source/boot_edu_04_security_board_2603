package boot_edu_04_security_board_2603.controller;

import boot_edu_04_security_board_2603.dto.BoardDTO;
import boot_edu_04_security_board_2603.dto.PageRequestDTO;
import boot_edu_04_security_board_2603.dto.PageResponseDTO;
import boot_edu_04_security_board_2603.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

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
    @PreAuthorize("hasRole('USER')") // 'USER' 이라는 권한이 있는지 확인
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

    // 로그인한 사용자만 조회할 수 있도록 수정
    // 조회
    @PreAuthorize("isAuthenticated()") // 인증 여부 확인
    @GetMapping("/read")
    public void read(Long bno, PageRequestDTO pageRequestDTO, Model model) {
        log.info("read get...");

        BoardDTO boardDTO = boardService.readOne(bno);
        log.info("boardDTO: {}", boardDTO);
        model.addAttribute("boardDTO", boardDTO);
    }

    // 수정
    @PreAuthorize("isAuthenticated()") // 인증 여부 확인
    @GetMapping("/modify")
    public String modify(Long bno, Principal principal, Model model) {
        log.info("modify get...");

        BoardDTO boardDTO = boardService.readOne(bno);
        // 작성자를 확인할 수 있는 시점
        log.info("principal name: {}", principal.getName());
        if (!principal.getName().equals(boardDTO.getWriter())) {
            // throw new AccessDeniedException("작성자가 아님");
            return "redirect:/member/login?error=ACCESS_DENIED";
        }
        log.info("modify get boardDTO: {}", boardDTO);
        model.addAttribute("boardDTO", boardDTO);
        return "/board/modify";
    }

    // 작성자만 접근 가능해야 함
    @PreAuthorize("isAuthenticated()")
    // 이런 방식으로 사용할 경우 작성자의 아이디가 화면에 노출되면 안됨
    // 마이 페이지 외의 페이지에 개인 정보가 가능하면 나오지 않도록 하는 것이 보안상 좋음
    @PostMapping("/modify")
    public String modify(@Valid BoardDTO boardDTO, BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         PageRequestDTO pageRequestDTO) {
        log.info("modify post...");
        // 1. 유효성 검사에서 문제가 있는 경우
        if (bindingResult.hasErrors()) {
            log.info(bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

            redirectAttributes.addAttribute("bno", boardDTO.getBno());
            return "redirect:/board/modify?" + pageRequestDTO.getLink();
        }

        // 2. 수정 작업
        log.info("boardDTO: {}", boardDTO);
        boardService.modify(boardDTO);

        redirectAttributes.addFlashAttribute("result", boardDTO.getBno()); // 세션에 값 저장
        redirectAttributes.addAttribute("bno", boardDTO.getBno());
        return "redirect:/board/read";
    }

    // 삭제
    @PreAuthorize("principal.username == #boardDTO.writer")
    @PostMapping("/remove")
    public String remove(Long bno, BoardDTO boardDTO, PageRequestDTO pageRequestDTO) {
        log.info("remove post...");

        boardService.remove(bno);
        return "redirect:/board/list?" + pageRequestDTO.getLink();
    }
}