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

	//フィールドの宣言
    private final UserDetailsService userDetailsService; //認証プロセスで使用するユーザー情報
    private final PasswordEncoder passwordEncoder;//パスワードのエンコードと照合を行う

    public CustomAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }//カスタム認証プロバイダーのコンストラクターを定義
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        
        String email  = authentication.getName(); // 入力された名前 emailを使用する
        String inputPassword = (String) authentication.getCredentials();//入力されたパスワード
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(email); // emailを使用してユーザー情報を取得する
        
        if (passwordEncoder.matches(inputPassword, userDetails.getPassword())) {
            return new UsernamePasswordAuthenticationToken(email, inputPassword, userDetails.getAuthorities());
          //DBのハッシュ化されたパスワードと一致するかを確認
        } else {
            throw new BadCredentialsException("Authentication failed");
            //スローすることで、認証が失敗したことを明示的に示す
        }
    }
    

    @Override
    public boolean supports(Class<?> authentication) {
        // authentication(認証方式)がUsernamePasswordAuthenticationToken.class(ユーザー名とパスワード認証)判定
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
