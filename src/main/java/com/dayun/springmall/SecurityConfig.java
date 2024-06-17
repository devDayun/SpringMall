package com.dayun.springmall;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // lamda expression, arrow operator 참고
        http.csrf((csrf) -> csrf.disable());
        http.authorizeHttpRequests((authorize) ->
                // 특정 페이지를 로그인 검사할 지 결정
                authorize.requestMatchers("/**").permitAll()
        );
        // 폼으로 로그인하겠다고 설정
        http.formLogin((formLogin) -> formLogin
                // 로그인 페이지 URL 적기
                .loginPage("/login")
                // 로그인 성공 시 이동할 URL, 실패 시 기본적으로 /login?error로 이동함(매개변수로 error가 전달됨)
                .defaultSuccessUrl("/list")
        );
        // 로그아웃
        http.logout(logout -> logout.logoutUrl("/logout"));
        return http.build();
    }
}
