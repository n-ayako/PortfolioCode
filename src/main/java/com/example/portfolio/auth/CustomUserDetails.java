package com.example.portfolio.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;  // Spring SecurityのUserクラスをインポート
import org.springframework.security.core.userdetails.UserDetails;

import com.example.portfolio.entity.Users;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {  // CustomUserDetailsクラスがUserクラスを拡張
	
    private Long id;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private String name;
		
    public CustomUserDetails(String email, String password, Collection<? extends GrantedAuthority> authorities, String name) {
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.name = name;
        
    }
    
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // 以下のメソッドは必要に応じて実装しますが、基本的には true を返します

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Long getId() {
        return this.id;
    }
}