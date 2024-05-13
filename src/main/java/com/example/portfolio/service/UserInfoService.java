package com.example.portfolio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.portfolio.dao.UsersMapper;
import com.example.portfolio.dto.UserAddRequest;
import com.example.portfolio.entity.Users;

@Service
public class UserInfoService {

    /**
     * ユーザー情報 Mapper
     */
    @Autowired
    private UsersMapper UsersMapper;

    /**
     * ユーザ情報登録
     * @param userAddRequest リクエストデータ
     */
    public void save(UserAddRequest userAddRequest) {
    	UsersMapper.save(userAddRequest);
    }

}
