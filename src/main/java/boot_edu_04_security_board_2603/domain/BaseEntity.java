package boot_edu_04_security_board_2603.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class}) // 필드 값이 자동으로 들어가도록 설정
public class BaseEntity {
    @CreatedDate // 생성될 때 자동 등록
    @Column(name = "reg_date", updatable = false) // 컬럼명은 reg_date, 업데이트 할 땐 사용하지 않을 것
    private LocalDateTime regDate;

    @LastModifiedDate // 최종 수정 날짜
    @Column(name = "mod_date")
    private LocalDateTime modDate;
}