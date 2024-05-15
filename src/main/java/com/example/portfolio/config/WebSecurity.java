package com.example.portfolio.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration//クラスが設定クラスであることを伝えるアノテーション
@EnableWebSecurity//Spring Securityのウェブセキュリティを有効にするアノテーション
public class WebSecurity {
    
    private final CustomAuthenticationProvider customAuthenticationProvider;

    public WebSecurity(CustomAuthenticationProvider customAuthenticationProvider){
        this.customAuthenticationProvider = customAuthenticationProvider;
    }
	
 // Loggerの取得
    private static final Logger logger = LoggerFactory.getLogger(WebSecurity.class);
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        // カスタム認証プロバイダを設定
        .authenticationProvider(customAuthenticationProvider)
        // CORSの設定を適用
        .cors(customizer -> customizer.configurationSource(corsConfigurationSource()))
        // CSRFの保護を無効にする
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(authorizeRequests ->
            authorizeRequests
                // loginのパスへのリクエストはすべて許可
                .requestMatchers("/login","/login?error","/signin","/css/**","/create/**").permitAll()
                 // その他のリクエストは認証が必要
                .anyRequest().authenticated()
        )
        .formLogin(formLogin -> 
        formLogin
            // ログイン処理が呼び出される前のログメッセージ
            .loginProcessingUrl("/login")
            .loginPage("/login")
            .successHandler((request, response, authentication) -> {
                // ログイン成功時の処理
                logger.info("Login successful: {}", authentication.getName());
                response.sendRedirect("/portfolio"); // ログイン成功後のリダイレクト先
            })
            .failureHandler((request, response, exception) -> {
                // ログイン失敗時の処理
                logger.error("Login failed: {}", exception.getMessage());
                response.sendRedirect("/login?error"); // ログイン失敗後のリダイレクト先
            })
            .permitAll()
    );
        return http.build();
    }
    
    // @Beanをつけることで、このメソッドがSpringのコンテナにBeanとして登録される
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        // CORSの設定を行うためのオブジェクトを生成
    	CorsConfiguration configuration = new CorsConfiguration();
    	configuration.setAllowCredentials(true);
    	configuration.addAllowedOrigin("http://localhost:8080/");
        configuration.addAllowedMethod("*"); // すべてのHTTPメソッドを許可
        configuration.addAllowedHeader("*"); // すべてのヘッダーを許可
        configuration.setAllowCredentials(true); // クレデンシャルを許可
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    
    
}
