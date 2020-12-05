package me.dktsudgg.demostudyrestapi.events;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 이미 "Event"라는 클래스가 있음에도 내가 원하는 입력값만 사용하기 위해 입력값 받는 Dto를 이렇게 따로 뺐음..
 * 입력값 검증 및 데이터 validation 로직을 위해서 사용..
 * 단점은 "Event"클래스와 중복이 생김..
 */
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class EventDto {

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

}
