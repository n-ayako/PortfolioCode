package com.example.portfolio.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.portfolio.entity.Users;

@Service
public class TCustomerUserDetails implements UserDetailsService {
    
    @Autowired
    private UsersMapper usersMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // メールアドレスをユーザー名として扱う
        String email = username;
        // MyBatisのMapperを使用してデータベースからユーザー情報を取得
        Users users = usersMapper.findByEmail(email);
        if (users == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        // ユーザー情報をUserDetailsオブジェクトに変換して返す
        return org.springframework.security.core.userdetails.User.builder()
                .username(users.getEmail())
                .password(users.getPassword())
                .roles("USER")
                .build();
    }
}