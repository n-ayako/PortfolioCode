package com.example.portfolio.service;

import java.util.List;

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

    /**
     * ユーザー情報 Mapper
     */
    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * ユーザ情報登録
     * @param userAddRequest リクエストデータ
     * userAddRequestからパスワード取得、PasswordEncoderを使用して、取得したパスワードを安全な形式でエンコード
     *エンコードされたパスワードをuserAddRequestオブジェクトに設定
     *UsersMapperを使用して、エンコードされたパスワードを含むuserAddRequestオブジェクトをデータベースに保存
     */
    public void save(UserAddRequest userAddRequest) {
        String encodedPassword = passwordEncoder.encode(userAddRequest.getPassword());
        userAddRequest.setPassword(encodedPassword);
        usersMapper.save(userAddRequest);
    }

}






