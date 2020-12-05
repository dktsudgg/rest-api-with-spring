package me.dktsudgg.demostudyrestapi.events;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

// 애노테이션들이 많을 경우에 하나의 애노테이션으로 묶을 수 있는데, 롬복은 그게 불가능해서 이렇게 다 작성
@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = "id") // equals랑 hashCode를 구현할 때 기본적으로는 모든 멤버변수를 다 사용하는데 stackoverflow 발생가능성 존재함. 따라서 id값만으로 구분. 중괄호표현식으로 여러개로 설정할 수 있음
@Entity
public class Event {

    @Id @GeneratedValue
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    private int basePrice;  // (optional)
    private int maxPrice;  // (optional)
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;

}
