package boot_edu_04_security_board_2603.service;

import boot_edu_04_security_board_2603.dto.BoardDTO;
import boot_edu_04_security_board_2603.dto.PageRequestDTO;
import boot_edu_04_security_board_2603.dto.PageResponseDTO;

public interface BoardService {
    // 등록
    Long add(BoardDTO boardDTO);

    // 조회
    BoardDTO readOne(Long bno);

    // 수정
    void modify(BoardDTO boardDTO);

    // 삭제
    void remove(Long bno);

    // 목록
    PageResponseDTO<BoardDTO> getList(PageRequestDTO pageRequestDTO);
}