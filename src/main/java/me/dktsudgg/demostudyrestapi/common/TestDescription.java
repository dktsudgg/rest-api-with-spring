package me.dktsudgg.demostudyrestapi.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * JUnit5에 @DisplayName 애노테이션으로 테스트코드에 대한 설명을 남길 수 있는데
 * JUnit4에서는 @DisplayName 애노테이션이 없으므로 이 애노테이션을 작성함.. 설명을 하기위한 용도로 붙여놓는 애노테이션일 뿐이므로 @DisplayName처럼 인텔리제이 Run test 시, 내용 출력까지 되진 않음
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface TestDescription {

    String value();

}
