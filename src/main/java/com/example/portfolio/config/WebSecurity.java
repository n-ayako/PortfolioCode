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

    @Bean
    //http.authorizeRequests(認証が必要となるURLを設定する関数)
    //antMatchers("〜").permitAll()は認証が不要の例外ページ
    //anyRequest().authenticated();以外のページは認証された状態でいる必要があるもの
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
        // 認証不要で誰でも見られるページ
        .requestMatchers("/login","/login?error","/signin","/css/**","/create/**").permitAll()
        // その他のリクエストは認証が必要
       .anyRequest().authenticated()
        )
        .formLogin(formLogin -> 
        formLogin
        	.usernameParameter("username")//ユーザのパラメータ名 POSTされてきたinputのname
        	.passwordParameter("password")//パスワードのパラメータ名 POSTされてきたinputのname
            .loginProcessingUrl("/login")//ログイン画面のURL
            .loginPage("/login")//ログイン画面のURL
            .failureUrl("/login?error")// ログイン失敗時のURL
            .defaultSuccessUrl("/portfolio", true)// ログインに成功した場合の遷移先
            // アクセス権
            .permitAll()
        )
        
            .logout((logout) -> logout
            // ログアウトした場合の遷移先
            .permitAll());
        
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
