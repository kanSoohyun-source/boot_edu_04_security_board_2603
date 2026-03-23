package boot_edu_01_board_2603.repository;

import boot_edu_04_security_board_2603.domain.Board;
import boot_edu_04_security_board_2603.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

@Log4j2
@SpringBootTest
class BoardRepositoryTest {
    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void testInsert() {
        /*
        DB에 insert를 실행하는 기능은 JpaRepository의 save() 통해 이루어짐
        save()는 현재의 영속 컨텍스트 내 데이터가 존재하는지 찾아보고,
        해당 엔티티 객체가 없을 때는 insert, 존재할 때는 update를 자동 실행
         */
        for (int i = 0; i < 100; i++) {
            Board board = Board.builder()
                    .title("title...")
                    .content("content... " + i)
                    .writer("user" + (i % 10)).build();

            Board result = boardRepository.save(board);
            log.info("bno: {}", result.getBno()); // bno를 따로 저장 안 했는데 실행하면 bno가 나오는지
        }
    }

    @Test
    public void testSelect() {
        /*
        특정 번호의 게시물을 조회하는 기능은 findById() 이용
        findById()의 리턴 타입은 Optional<T>
         */
        Long bno = 100L;
        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();
        log.info(board);
    }

    @Test
    @Transactional
    public void testSelect2() {
        /*
        getReferenceById()
        1) 특정한 번호의 게시물을 조회하는 기능
        2) 반환 타입은 Optional<T>가 아니라 T
        3) 지연 조회 : 호출 시 DB에 쿼리를 날리는 것이 아니라 사용 시점에 쿼리를 날림
         */
        Long bno = 10L;
        Board board = boardRepository.getReferenceById(bno);
        // 사용 시점에서 쿼리를 날리는 경우 엔티티 매니저가 닫혀서 LazyInitializationException 발생
        log.info("------------"); // 쿼리 날리는 지점 확인 위한 의미 없는 출력
        log.info(board); // ** 여기가 쿼리 날리는 지점 **
        // 해결 방안 : @Transactional 사용해 메서드가 끝날때까지 DB 연결되어 있게 할 것
    }

    @Test
    public void testSear1() {
        String keyword = "6";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        Page<Board> result = boardRepository.findByContentContaining(keyword, pageable);
        for (Board board : result.getContent()) {
            log.info(board);
        }
    }

    @Test
    public void testSear2() {
        String keyword = "6";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        Page<Board> result = boardRepository.findContent(keyword, pageable);
        for (Board board : result.getContent()) {
            log.info(board);
        }
    }

    @Test
    public void testUpdate() {
        /*
        title, content만 수정 대상
        그러나 update 관련 작성할 때 수정 대상만 넣으면 발생할 수 있는 문제가 있음
        writer의 속성이 nullable = false이기 때문에 엔티티-DB 동기화로 인해 writer 값 지정이 없으면 에러 터짐
        엔티티 객체는 update이더라도 엔티티 객체 전체가 들어가야 함
         */
        Long bno = 100L;
        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();
        board.change("제목 수정 중", "내용 수정 중", "작성자 수정 중");
        boardRepository.save(board);
    }

    @Test
    public void testUpdate2() {
        // 없는 bno로 save 했을 때
        Long bno = 200L;
        Board board = Board.builder()
                .bno(bno)
                .title("title...")
                .content("content... ")
                .writer("user").build();
        boardRepository.save(board);
    }

    @Test
    public void testDelete() {
        /*
        delete는 @Id에 해당하는 값으로 deleteById() 통해 실행
         */
        Long bno = 1L;
        boardRepository.deleteById(bno);
    }

    @Test
    public void testPaging() {
        // 1 page 10 posts, order by bno desc
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        Page<Board> result = boardRepository.findAll(pageable);

        for (Board board : result.getContent()) {
            log.info(board);
        }

        log.info("total count: {}", result.getTotalElements()); // 전체 데이터 개수
        log.info("total pages: {}", result.getTotalPages()); // 전체 페이지 수
        log.info("page number: {}", result.getNumber());
        log.info("page size: {}", result.getSize());

        log.info(result.hasNext());
        log.info(result.hasPrevious());
    }

    @Test
    public void testQuerydsl1() {
        Pageable pageable = PageRequest.of(0, 10);
        boardRepository.search1(pageable);
    }

    @Test
    public void testSearchAll() {
        String[] types = {"c"};
        String keyword = "5";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

        for (Board board : result.getContent()) {
            log.info(board);
        }

        log.info("total count: {}", result.getTotalElements()); // 전체 데이터 개수
        log.info("total pages: {}", result.getTotalPages()); // 전체 페이지 수
        log.info("page number: {}", result.getNumber());
        log.info("page size: {}", result.getSize());

        log.info(result.hasNext());
        log.info(result.hasPrevious());
    }
}