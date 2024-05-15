//データベースから取得したユーザ情報をSpring Securityが認識できる形式に変換する
package com.example.portfolio.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.portfolio.dao.UsersMapper;
import com.example.portfolio.entity.Users;

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
        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}
