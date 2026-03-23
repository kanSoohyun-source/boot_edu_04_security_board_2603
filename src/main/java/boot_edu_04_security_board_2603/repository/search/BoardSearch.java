package boot_edu_04_security_board_2603.repository.search;

import boot_edu_04_security_board_2603.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch {
    /*
    1. Querydsl을 이용할 인터페이스 선언
    2. '인터페이스 + Impl' 이름의 클래스 선언
        - 이때 QuerydslRepositorySupport이라는 부모 클래스 지정하고 인터페이스 구현
    3. 기존 Repository에 부모 인터페이스로 Querydsl 위한 인터페이스 지정
     */
    Page<Board> search1(Pageable pageable);
    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);
}
