package boot_edu_04_security_board_2603.security.dto;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Data
public class MemberSecurityDTO extends User {
    /*
    MemberSecurityDTO 클래스는 org.springframework.security.core.userdetails.User라는 클래스는 부모 클래스로 사용
    User 클래스는 UserDetails 인터페이스를 구현한 ㅋ클래스로 최대한 간단하게 UserDetails 타입을 생성할 수 있는 방법을 제공
     */
    private String mid;
    private String mpw;
    private String email;
    private boolean del;
    private boolean social;
    private String username;

    /*
    Collection<? extends GrantedAuthority> authorities
    -> GrantedAuthority 을 상속받는 타입의 컬렉션, 제네릭에 타입을 한정
     */
    public MemberSecurityDTO(String username,
                             String password,
                             String email ,
                             boolean del,
                             boolean social,
                             Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);

        this.mid = username;
        this.mpw = password;
        this.email = email;
        this.del = del;
        this.social = social;
        this.username = username;
    }

}
