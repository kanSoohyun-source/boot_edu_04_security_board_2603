package boot_edu_01_board_2603.service;

import boot_edu_01_board_2603.domain.Board;
import boot_edu_01_board_2603.dto.BoardDTO;
import boot_edu_01_board_2603.dto.PageRequestDTO;
import boot_edu_01_board_2603.dto.PageResponseDTO;
import boot_edu_01_board_2603.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Log4j2
@Service
@RequiredArgsConstructor // 생성자 매개변수 대체 어노테이션
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;

    @Override
    public Long add(BoardDTO boardDTO) {
        Board result = boardRepository.save(modelMapper.map(boardDTO, Board.class));
        return result.getBno();
    }

    @Override
    public BoardDTO readOne(Long bno) {
        BoardDTO boardDTO = modelMapper.map(boardRepository.findById(bno), BoardDTO.class);
        return boardDTO;
    }

    @Override
    public void modify(BoardDTO boardDTO) {
        // 수정 시 boardDTO에 필요한 데이터 -> bno, title, content
        // jpa의 save 이용 할 땐 Board 엔티티의 모든 데이터가 있어야 함
        // 03.05 ++ 수정 사항에 writer 추가
        Optional<Board> optionalBoard = boardRepository.findById(boardDTO.getBno());
        Board board = optionalBoard.orElseThrow(); // orElseThrow() : 값이 존재하는 경우 값 반환, 그 외 예외 발생
        board.change(boardDTO.getTitle(), boardDTO.getContent(), boardDTO.getWriter());
        boardRepository.save(board);
    }

    @Override
    public void remove(Long bno) {
        boardRepository.deleteById(bno);
    }

    @Override
    public PageResponseDTO<BoardDTO> getList(PageRequestDTO pageRequestDTO) {
        // 1. boardRepository.searchAll() 실행이 목표
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageAble("bno");
        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

        // 2. return 위해 PageResponseDTO 생성
        List<BoardDTO> dtoList = new ArrayList<>();
        for (Board board : result.getContent()) {
            dtoList.add(modelMapper.map(board, BoardDTO.class));
        }

        return PageResponseDTO.<BoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .total((int) result.getTotalElements())
                .dtoList(dtoList).build();
    }
}