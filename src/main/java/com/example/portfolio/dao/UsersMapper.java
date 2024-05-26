package com.example.portfolio.dao;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.example.portfolio.auth.CustomUserDetails;
import com.example.portfolio.dto.UserAddRequest;
import com.example.portfolio.dto.UserProfileEdit;
import com.example.portfolio.entity.Users;

@Mapper
public interface UsersMapper {
	
    /**
     * ユーザー情報登録
     * @param userRequest 登録用リクエストデータ
     */
    void save(UserAddRequest userAddRequest);
    
    /**
     * メールアドレスに基づいてユーザー情報を取得
     * @param email メールアドレス
     * @return ユーザー情報
     */
    Users findByUsername(String email);
    
    public Optional<UserProfileEdit> createUserProfileEdit(long id);
	
    public int updateProfile(UserProfileEdit userProfileEdit);
    
}


