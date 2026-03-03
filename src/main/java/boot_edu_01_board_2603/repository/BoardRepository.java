package boot_edu_01_board_2603.repository;

import boot_edu_01_board_2603.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("select b from Board b where b.title like concat('%', :keyword, '%')")
    Page<Board> findKeyword(String keyword, Pageable pageable);
}