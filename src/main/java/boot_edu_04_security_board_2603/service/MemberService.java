package boot_edu_04_security_board_2603.service;

import boot_edu_04_security_board_2603.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public boolean checkMid(String mid) {
        return memberRepository.existsById(mid);
    }
}
