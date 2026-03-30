package boot_edu_04_security_board_2603.repository;

import boot_edu_04_security_board_2603.domain.Member;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    // 로그인시에 사용, 소셜 여부와 탈퇴 여부가 조건절에 들어감

    @EntityGraph(attributePaths = "roleSet")
    @Query("SELECT m FROM Member m WHERE m.mid = :mid AND m.del = false")
    Optional<Member> getWithRoles(String mid);

    @EntityGraph(attributePaths = "roleSet")
    Optional<Member> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE Member m SET m.mpw =:mpw WHERE m.mid = :mid")
    void updateWithPassword(@Param("mid") String mid,@Param("mpw") String mpw);
}
