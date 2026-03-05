package boot_edu_01_board_2603.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 키 생성 전략 (IDENTITY : DB에서 알아서 결정하는 방식 (우리는 mariaDB의 auto_increment 사용))
    private Long bno;

    @Column(length = 500, nullable = false) // 컬럼 길이, null 허용 여부 설정
    private String title;

    @Column(length = 2000, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;

    public void change(String title, String content, String writer) {
        this.title = title;
        this.content = content;
        this.writer = writer;
    }
}