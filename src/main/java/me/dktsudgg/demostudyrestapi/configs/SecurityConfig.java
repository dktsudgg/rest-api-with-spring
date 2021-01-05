package me.dktsudgg.demostudyrestapi.configs;

import me.dktsudgg.demostudyrestapi.accounts.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService)
                .passwordEncoder(passwordEncoder);
    }

    /**
     * 1. 모든 요청은 스프링 시큐리티 인증이 걸리게 되는데
     * 여기에 스프링 시큐리티가 적용되지 않을 것들을 정의
     * static 리소스들을 전부 허용할 거라면 여기서 걸러주는게 서버 부하가 덜 일어나므로 이것을 사용
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers("/docs/index.html");
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());    // static 리소스
    }

    /**
     * 2. 시큐리티는 적용 하되, http에서 거르는 방법
     * 리소스 서버 설정에서 비슷한 설정을 할 것이기 때문에 주석처리..
     */
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
////        http.authorizeRequests()
////                .mvcMatchers("/docs/index.html").anonymous()
////                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).anonymous()
////        ;
//
//        /**
//         * 익명사용자 허용, 폼인증을 사용, 익명사용자에게 허용할 HTTP 메소드를 작성, 나머지는 인증이 필요
//         */
//        http
//            .anonymous()
//                .and()
//            .formLogin()
//                .and()
//            .authorizeRequests()
//                .mvcMatchers(HttpMethod.GET, "/api/**").anonymous()
//                .anyRequest().authenticated();
//    }

}
