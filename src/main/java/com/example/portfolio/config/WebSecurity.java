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
                    )
                    .formLogin((login) -> login
                        .usernameParameter("username")
                        .passwordParameter("password")
                        // ログインを実行するページ
                        .loginProcessingUrl("/login")
                        // ログイン画面
                        .loginPage("/login")
                        // ログイン失敗時のURL
                        .failureUrl("/login?error")
                        // ログインに成功した場合の遷移先
                        .defaultSuccessUrl("/loginSuccess", true)
                        // アクセス権
                        .permitAll()

                    )
                    .logout((logout) -> logout
                         // ログアウトした場合の遷移先
                        .permitAll());
        
        return http.build();
    }
	
    // パスワードのハッシュ化
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
