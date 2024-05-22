//データベースから取得したユーザ情報をSpring Securityが認識できる形式に変換する
package com.example.portfolio.service;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.portfolio.entity.Users;
import com.example.portfolio.auth.CustomUserDetails;


@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserInfoService userInfoService;
    public CustomUserDetailsService(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = userInfoService.getUserByUsername(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + email);
        }
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("USER"));
        return new CustomUserDetails(user.getEmail(), user.getPassword(), authorities, user.getName());
    }
    
}

