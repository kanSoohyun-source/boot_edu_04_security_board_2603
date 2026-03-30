package boot_edu_04_security_board_2603.service;

import boot_edu_04_security_board_2603.dto.MemberJoinDTO;

public interface MemberService {

    static class MidExistException extends Exception {

    }

    void join(MemberJoinDTO memberJoinDTO) throws MidExistException;

    // 아이디 중복 확인
    boolean isDuplicateMid(String mid);

    void modifyPassword(String mid, String mpw) throws MidExistException;
}
