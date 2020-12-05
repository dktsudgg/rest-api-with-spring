package me.dktsudgg.demostudyrestapi.events;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 이미 "Event"라는 클래스가 있음에도 내가 원하는 입력값만 사용하기 위해 입력값 받는 Dto를 이렇게 따로 뺐음..
 * 입력값 검증 및 데이터 validation 로직을 위해서 사용..
 * 단점은 "Event"클래스와 중복이 생김..
 */
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class EventDto {

    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotNull
    private LocalDateTime beginEnrollmentDateTime;
    @NotNull
    private LocalDateTime closeEnrollmentDateTime;
    @NotNull
    private LocalDateTime beginEventDateTime;
    @NotNull
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    @Min(0)
    private int basePrice;  // (optional)
    @Min(0)
    private int maxPrice;  // (optional)
    @Min(0)
    private int limitOfEnrollment;

}
