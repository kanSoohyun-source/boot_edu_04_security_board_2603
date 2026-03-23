package boot_edu_04_security_board_2603.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PageResponseDTO<E> {
    private int page;
    private int size;
    private int total; // 전체 게시물 숫자

    private int start; // 시작 페이지 번호
    private int end;   // 끝 페이지 번호

    private boolean prev; // 이전 페이지 존재 여부
    private boolean next; // 다음 페이지 존재 여부

    private List<E> dtoList; // 목록 데이터(회원 데이터, 주차 데이터 등 범용적으로 쓸 수 있음)

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(PageRequestDTO pageRequestDTO, int total, List<E> dtoList) {
        this.page = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;

        // 받은 값을 통해 남은 변수 계산
        int perPage = 5;
        this.end = (int) (Math.ceil(this.page / (double) perPage) * perPage);
        this.start = this.end - 4;

//        if (this.page > perPage / 2) {
//            this.end = this.page + (perPage / 2);
//            this.start = this.page - (perPage / 2);
//        }

        int last = (int) (Math.ceil(total / (double) size)); // 게시물 기준 마지막 페이지
        this.end = Math.min(end, last);

        // 이전 prev, 다음 next 계산
        this.prev = this.start > 1; // 시작 페이지가 1이 아니면 prev는 무조건 true
        this.next = total > this.end * this.size;
    }
}