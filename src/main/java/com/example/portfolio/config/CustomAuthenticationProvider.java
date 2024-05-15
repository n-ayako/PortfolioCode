package com.example.portfolio.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// @Componentをつけることで、このクラスがSpringのコンテナにBeanとして登録される
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider{

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // ログイン処理が呼び出される前のログメッセージを出力
        System.out.println("ログイン処理が呼び出される前のログメッセージ");
        
        String email  = authentication.getName(); // ユーザー名をemailとして使用する
        String inputPassword = (String) authentication.getCredentials();
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(email); // emailを使用してユーザー情報を取得する

        // ログ出力：入力されたパスワードとデータベースから取得したハッシュ化されたパスワードの比較
        System.out.println("Input Password: " + inputPassword);
        System.out.println("Database Password: " + userDetails.getPassword());
        //でバックコードここまで
        
        if (passwordEncoder.matches(inputPassword, userDetails.getPassword())) {//DBのハッシュ化されたパスワードと一致するかを確認
            return new UsernamePasswordAuthenticationToken(email, inputPassword, userDetails.getAuthorities());
        } else {
            throw new BadCredentialsException("Authentication failed");
        }
    }
    

    @Override
    public boolean supports(Class<?> authentication) {
        // authentication(認証方式)がUsernamePasswordAuthenticationToken.class(ユーザー名とパスワード認証)か判定
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
