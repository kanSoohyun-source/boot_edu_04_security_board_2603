package boot_edu_01_board_2603.service;

import boot_edu_04_security_board_2603.dto.BoardDTO;
import boot_edu_04_security_board_2603.dto.PageRequestDTO;
import boot_edu_04_security_board_2603.dto.PageResponseDTO;
import boot_edu_04_security_board_2603.service.BoardService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Log4j2
@SpringBootTest
class BoardServiceImplTest {
    @Autowired
    private BoardService boardService;

    @Test
    public void testAdd() {
        BoardDTO boardDTO = BoardDTO.builder()
                .title("dto INSERT")
                .content("배고프다")
                .writer("민정").build();
        Long bno = boardService.add(boardDTO);
        log.info(bno);
    }

    @Test
    public void testReadOne() {
        Long bno = 101L;
        BoardDTO boardDTO = boardService.readOne(bno);
        log.info(boardDTO);
    }

    @Test
    public void testModify() {
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(102L).title("수정할 거임").content("밥 먹고싶음").build();
        boardService.modify(boardDTO);
    }

    @Test
    public void testRemove() {
        Long bno = 100L;
        boardService.remove(bno);
    }

    @Test
    public void testGetList() {
        /*
        테스트 코드 작성 패턴 GWT(Given-When-Then)
         */
        // 1. Given : 테스트 수행 전 준비 단계 (데이터 생성)
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(2).size(10)
                .type("c").keyword("5").build();

        // 2. when : 실제 테스트 동작 실행
        PageResponseDTO<BoardDTO> result = boardService.getList(pageRequestDTO);

        // 3. then : 예상과 일치하는지 검증
        List<BoardDTO> dtoList = result.getDtoList();
        dtoList.forEach(it -> log.info(it));
    }

    @Test
    public void testGetList2() {
        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        PageResponseDTO<BoardDTO> result = boardService.getList(pageRequestDTO);
        log.info(result);
        for (BoardDTO boardDTO : result.getDtoList()) {
            log.info(boardDTO);
        }
    }
}