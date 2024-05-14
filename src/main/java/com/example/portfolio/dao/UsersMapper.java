package com.example.portfolio.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.example.portfolio.dto.UserAddRequest;
import com.example.portfolio.entity.Users;

@Mapper
public interface UsersMapper {
	
    /**
     * ユーザー情報登録
     * @param userRequest 登録用リクエストデータ
     */
    void save(UserAddRequest userAddRequest);//保存する
    
    /*public MyEmail findByEmail(String email);//識別する*/
    Users findByEmail(String email);
}