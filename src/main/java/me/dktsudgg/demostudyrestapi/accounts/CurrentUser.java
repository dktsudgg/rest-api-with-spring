package me.dktsudgg.demostudyrestapi.accounts;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)      // 파라미터에 붙일 수 있고,
@Retention(RetentionPolicy.RUNTIME) // 언제까지 이 애노테이션 정보를 유지할 것인가
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account")    // // AccountAdapter가 가진 객체 중에 "account"라는 필드값을 꺼내서 주입해줌.. 익명사용자인 경우에는 principle이 문자열인데 그때는 null을 리턴하도록..
public @interface CurrentUser {
}
