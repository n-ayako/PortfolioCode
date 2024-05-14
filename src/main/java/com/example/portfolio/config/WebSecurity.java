package com.example.portfolio.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity//SpringSecurityによるフォーム認証を有効化
public class WebSecurity{
	
	@Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                // アクセス制限をかけない
                .requestMatchers(
                		"/"
                        , "/signin"
                        ,"/login"
                        ,"/logout"                        
                        ,"/login?error"
                        ,"/css/**")
                //認証なしでアクセス可
                .permitAll()
                .anyRequest().authenticated()
            );
        	//HttpSecurityオブジェクトからSecurityFilterChainオブジェクトを構築して返す操作	
        return http.build();
    }
	
    // パスワードのハッシュ化
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
