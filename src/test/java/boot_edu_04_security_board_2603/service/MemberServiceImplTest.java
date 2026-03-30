package boot_edu_04_security_board_2603.service;

import boot_edu_04_security_board_2603.dto.MemberJoinDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@SpringBootTest
class MemberServiceImplTest {
    @Autowired
    private MemberService memberService;

    @Test
    void joinTest() {
        MemberJoinDTO memberJoinDTO = new MemberJoinDTO();
        memberJoinDTO.setMid("user");
        memberJoinDTO.setMpw("1111");
        memberJoinDTO.setEmail("user@email.com");

        try {
            memberService.join(memberJoinDTO);
        } catch (MemberService.MidExistException e) {
            log.info("아이디 중복");
        }
    }
}