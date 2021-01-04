package me.dktsudgg.demostudyrestapi.accounts;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class Account {

    @Id @GeneratedValue
    private Integer id;

    private String email;

    private String password;

    // 기본으로 모든 Set, List는 Lazy Patch인데, 이 경우에는 가져올 롤이 매우 적고 거의 매번 이 어카운트를 가져올 때 마다 필요한 정보라서 Eager모드로 패치 설정
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<AccountRole> roles;

}
