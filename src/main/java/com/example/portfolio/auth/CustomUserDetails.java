package com.example.portfolio.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;  // Spring SecurityのUserクラスをインポート
import org.springframework.security.core.userdetails.UserDetails;

import com.example.portfolio.entity.Users;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {  // CustomUserDetailsクラスがUserクラスを拡張
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
    private String email;
    private String password;
    private String name;
    private String selfIntroduction;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails( Long id,String email, String password, String name,String selfIntroduction, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.selfIntroduction = selfIntroduction;
        this.authorities = authorities;
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
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
	public String getSelfIntroduction() {
		return selfIntroduction;
	}
	public void setSelfIntroduction(String selfIntroduction) {
		this.selfIntroduction = selfIntroduction;
	}
}