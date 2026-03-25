package boot_edu_04_security_board_2603.domain;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@ToString(exclude = "roleSet")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Member extends BaseEntity {
    @Id
    private String mid;

    private String mpw;
    private String email;
    // 탈퇴 여부(회원 탈퇴를 해도 데이터 삭제는 안 함, 탈퇴한 아니디로는 회원가입이 안됨)
    // 로그인 처리 시에 del 관련 처리가 sql 조건절이 들어감
    // 일반 회원, sns 로그인 회원을 분리
    private boolean del;
    private boolean social; // sns 가입(로그인) 여부

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>();

    // 비밀번호 수정
    public void changePasswd(String mpw) {
        this.mpw = mpw;
    }

    // 이메일 수정
    public void changeEmail(String email) {
        this.email = email;
    }

    // 탈퇴 처리
    public void changeDel(boolean del) {
        this.del = del;
    }

    // 소셜 처리(수정)
    public void changeSocial(boolean social) {
        this.social = social;
    }

    // role 추가
    public void addRole(MemberRole role) {
        roleSet.add(role);
    }

    // Role 삭제
    public void removeRole(MemberRole role) {
        roleSet.remove(role);
    }
}
