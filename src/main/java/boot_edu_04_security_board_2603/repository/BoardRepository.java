package boot_edu_04_security_board_2603.repository;

import boot_edu_04_security_board_2603.domain.Board;
import boot_edu_04_security_board_2603.repository.search.BoardSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

// 3. 기존 Repository에 부모 인터페이스로 Querydsl 위한 인터페이스 지정
// 인터페이스이기 때문에 다중 상속 가능
public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {

//    @Query("select b from Board b where b.title like concat('%', :keyword, '%')")
//    Page<Board> findKeyword(String keyword, Pageable pageable)

    /*
    [쿼리 메서드] - 메서드 이름 자체가 쿼리의 구문으로 처리되는 기능
    */
    // 게시판 내용에서 "6"이 포함된 게시물 검색 (content LIKE '%keyword%')
    Page<Board> findByContentContaining(String keyword, Pageable pageable);

    /*
    [JPQL] - @Query를 사용해 SQL과 유사하게 엔티티 클래스의 정보를 이용해 쿼리 작성하는 기능
     */
    @Query("SELECT b FROM Board b WHERE b.content like concat('%', :keyword, '%')")
    Page<Board> findContent(String keyword, Pageable pageable);
}