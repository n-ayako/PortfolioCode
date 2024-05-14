package com.example.portfolio.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.ui.Model;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.portfolio.dao.UsersMapper;
import com.example.portfolio.dto.UserAddRequest;
import com.example.portfolio.entity.Users;
import com.example.portfolio.securityUser.MyEmail;

import com.example.portfolio.dao.TCustomerUserDetails;

@Service
@Transactional
public class UserInfoService   {
	//DB接続をするMapperクラスを参照
    @Autowired
    private UsersMapper usersMapper;
    
    //パスワードのハッシュ化
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * ユーザ情報登録実施内容
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






