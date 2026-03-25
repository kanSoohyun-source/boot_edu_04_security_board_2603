package boot_edu_04_security_board_2603.repository;

import boot_edu_04_security_board_2603.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    // 로그인시에 사용, 소셜 여부와 탈퇴 여부가 조건절에 들어감

    @EntityGraph(attributePaths = "roleSet")
    @Query("SELECT m FROM Member m WHERE m.mid = :mid AND m.del = false AND m.social = false ")
    Optional<Member> getWithRoles(String mid);
}
