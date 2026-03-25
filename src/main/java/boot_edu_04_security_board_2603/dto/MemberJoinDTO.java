package boot_edu_04_security_board_2603.dto;

import lombok.Data;

@Data
public class MemberJoinDTO {
    /* 일반, 소셜에도 사용 */
    private String mid;
    private String mpw;
    private String email;
    private boolean del;
    // 직접 회원 가입 하는 경우는 소셜 회원을 의미하는 social 값은 false
    private boolean social;
}
