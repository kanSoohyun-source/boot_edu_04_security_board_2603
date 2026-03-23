package boot_edu_04_security_board_2603.repository.search;

import boot_edu_04_security_board_2603.domain.Board;
import boot_edu_04_security_board_2603.domain.QBoard;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {
    /*
    2. '인터페이스 + Impl' 이름의 클래스 선언
        - 이때 QuerydslRepositorySupport이라는 부모 클래스 지정(=상속 받아라)하고 인터페이스 구현
     */
    public BoardSearchImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> search1(Pageable pageable) {
        QBoard qBoard = QBoard.board; // Q도메인 객체 생성

        // 1. 쿼리문 생성
        JPQLQuery<Board> query = from(qBoard);   // select b from Board b
//        query.where(qBoard.title.contains("1"));

        /* 2. '제목'이나 '내용'에 특정 키워드가 존재하고 bno가 0보다 큰 데이터 찾기
         select * from board where (title like concat('%', '1', '%')
                                    or content like concat('%', '1', '%')) and bno > 0
         */
        // (1) 1번 조건 (title like concat('%', '1', '%') or content like concat('%', '1', '%'))
        BooleanBuilder booleanBuilder = new BooleanBuilder(); // ( ) 괄호 역할
        booleanBuilder.or(qBoard.title.contains("1"));   // title like concat('%', '1', '%')
        booleanBuilder.or(qBoard.content.contains("1")); // content like concat('%', '1', '%')
        query.where(booleanBuilder);

        // (2) 2번 조건 bno > 0
        query.where(qBoard.bno.gt(0L));

        // paging 처리
        this.getQuerydsl().applyPagination(pageable, query);

        // fetch : 쿼리 실행
        List<Board> boardList = query.fetch();

        // fetchCount : 전체 게시물 숫자
        long count = query.fetchCount();
        return null;
    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {
        // 검색 조건을 의미하는 types는 '제목(t), 내용(c), 작성자(w)'로 구성된다고 가정하고 이를 반영
        QBoard qBoard = QBoard.board;
        JPQLQuery<Board> query = from(qBoard);

        // 검색 조건, 키워드가 있다면
        if ((types != null && types.length > 0) && (keyword != null && !keyword.isEmpty())) {
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            for (String type : types) {
                switch (type){
                    case "t":
                        booleanBuilder.or(qBoard.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(qBoard.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(qBoard.writer.contains(keyword));
                        break;
                }
            }
            query.where(booleanBuilder);
        }

        query.where(qBoard.bno.gt(0L));

        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> boardList = query.fetch();
        long count = query.fetchCount();

        return new PageImpl<>(boardList, pageable, count);
    }
}
