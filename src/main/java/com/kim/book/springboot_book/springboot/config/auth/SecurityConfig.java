package com.kim.book.springboot_book.springboot.config.auth;

import com.kim.book.springboot_book.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .headers(headers -> headers.frameOptions(frame -> frame.disable())) // H2 콘솔 사용을 위한 FrameOptions 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile").permitAll() // 특정 경로 허용
                        .requestMatchers("/api/v1/**").hasRole(Role.USER.name()) // USER 권한만 접근 가능
                        .anyRequest().authenticated() // 그 외 요청은 인증 필요
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/") // 로그아웃 후 루트 경로로 이동
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) // OAuth2 사용자 서비스 설정
                        )
                );

        return http.build();
    }
}

