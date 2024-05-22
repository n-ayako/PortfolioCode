package com.example.portfolio.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.example.portfolio.dto.UserAddRequest;
import com.example.portfolio.entity.Users;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.example.portfolio.service.UserInfoService;
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
    
}


