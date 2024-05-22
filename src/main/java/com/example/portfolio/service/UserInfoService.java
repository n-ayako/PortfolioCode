package com.example.portfolio.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.portfolio.dao.UsersMapper;
import com.example.portfolio.dto.UserAddRequest;
import com.example.portfolio.entity.Users;

@Service
@Transactional
public class UserInfoService {

    private final UsersMapper usersMapper;

    public UserInfoService(UsersMapper usersMapper) {
        this.usersMapper = usersMapper;
    }

    @Autowired//
    private PasswordEncoder passwordEncoder;

    //ユーザー認証
    public Users getUserByUsername(String email) {
        return usersMapper.findByUsername(email);
    }
   
    
    //ユーザー新規登録    
    public void save(UserAddRequest userAddRequest) {
        String encodedPassword = passwordEncoder.encode(userAddRequest.getPassword());
        userAddRequest.setPassword(encodedPassword);
        usersMapper.save(userAddRequest);
    }
    
    
    

}