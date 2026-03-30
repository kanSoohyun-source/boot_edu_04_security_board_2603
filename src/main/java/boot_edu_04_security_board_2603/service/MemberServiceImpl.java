package boot_edu_04_security_board_2603.service;

import boot_edu_04_security_board_2603.domain.Member;
import boot_edu_04_security_board_2603.domain.MemberRole;
import boot_edu_04_security_board_2603.dto.MemberJoinDTO;
import boot_edu_04_security_board_2603.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void join(MemberJoinDTO memberJoinDTO) throws MidExistException {
        // 1. mid 가 존재하는 경우에는 midExistException 을 발생하도록 구성
        if (memberRepository.existsById(memberJoinDTO.getMid())) {
            throw new MidExistException();
        }

        // 2. 정상적으로 회원 가입되는 경우에는 PasswordEncoder 를 이용해서
        // 입력한 패스워드를 인코딩하도록 함
        Member member = modelMapper.map(memberJoinDTO, Member.class);
        // 비밀번호 암호화
        member.changePasswd(passwordEncoder.encode(memberJoinDTO.getMpw()));
        // 롤 부여
        member.addRole(MemberRole.USER);

        log.info("=============================");
        log.info(member);
        log.info(member.getRoleSet());

        memberRepository.save(member);
    }

    @Override
    public boolean isDuplicateMid(String mid) {
        return memberRepository.existsById(mid);
    }

    @Override
    public void modifyPassword(String mid, String mpw) throws MidExistException {
        Member member = memberRepository.findById(mid).orElseThrow();
        member.changePasswd(passwordEncoder.encode(mpw));
    }
}
