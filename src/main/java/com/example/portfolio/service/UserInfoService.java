package com.example.portfolio.service;

import java.util.Optional;

import org.apache.catalina.mapper.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.portfolio.auth.CustomUserDetails;
import com.example.portfolio.dao.UsersMapper;
import com.example.portfolio.dto.UserAddRequest;
import com.example.portfolio.dto.UserProfileEdit;
import com.example.portfolio.entity.Users;

import jakarta.validation.Valid;


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
    
    //最新のユーザー情報を取得する

    
    //プロフィール更新
    //写真の追加の際はここに写真の情報を追記する
	private CustomUserDetails user;
	
    public UserProfileEdit ProfileInfo(CustomUserDetails user) {
		// ユーザーオブジェクトをフィールドに保持
		this.user = user;
		// ユーザーの情報を取得
		Optional<UserProfileEdit> optionalForm = usersMapper.createUserProfileEdit(user.getId());
		return optionalForm.orElse(new UserProfileEdit());
	}
	
	@Transactional(rollbackFor = Throwable.class)
	public void updateProfile(UserProfileEdit form) throws Exception {
	    // DB更新件数
	    int updateCount = 0;
	    
	    // プロフィール更新
	    form.setId(user.getId());
	    updateCount += usersMapper.updateProfile(form);

	    if (updateCount != 1) {
	        throw new Exception("個人情報の更新に失敗しました。");
	    }
	}
    

}