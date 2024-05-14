package com.example.portfolio.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
                .defaultSuccessUrl("/portfolio", true)
                // アクセス権
                .permitAll()
            )
        .logout((logout) -> logout
                // ログアウトした場合の遷移先
                .permitAll());

		//HttpSecurityオブジェクトからSecurityFilterChainオブジェクトを構築して返す操作	
        return http.build();
    }
	
    // パスワードのハッシュ化
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
