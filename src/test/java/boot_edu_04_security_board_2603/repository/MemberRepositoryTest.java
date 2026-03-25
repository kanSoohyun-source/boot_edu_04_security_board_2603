package boot_edu_04_security_board_2603.repository;

import boot_edu_04_security_board_2603.domain.Member;
import boot_edu_04_security_board_2603.domain.MemberRole;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@SpringBootTest
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void insertMemberTest() {
        // 더미 회원 등록
        IntStream.rangeClosed(1, 100).forEach(i -> {
           Member member = Member.builder()
                   .mid("Member" + i)
                   .mpw(passwordEncoder.encode("1111"))
                   .email("email" + i + "@naver.com")
                   .build();
           member.addRole(MemberRole.USER); // 기본으로 유저 권한 부여

           if (i >= 90) {
               member.addRole(MemberRole.ADMIN); // 90번 이상의 회원은 ADMIN 권한 부여
           }
           memberRepository.save(member);
        });
    }

    @Test
    void selectMemberTest() {
        // 회원 조회 테스트
        Optional<Member> result = memberRepository.getWithRoles("Member100");
        Member member = result.orElseThrow();
        log.info(member);
        log.info(member.getRoleSet());

        member.getRoleSet().forEach(role -> log.info(role.name()));
    }

}