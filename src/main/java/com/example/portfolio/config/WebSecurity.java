package com.example.portfolio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurity {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                // アクセス制限をかけない
                .requestMatchers("/"
                        , "/signin"
                        , "/css/**"
                        , "/create/**"
                        ,"/portfolio")
                .permitAll()
                .anyRequest().authenticated()
            );
        // H2コンソール操作用
        http.csrf().disable();
        http.headers().frameOptions().disable();
        
        return http.build();
    }
	
    // パスワードのハッシュ化
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
