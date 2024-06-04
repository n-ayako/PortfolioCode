package com.example.portfolio.dao;

import java.util.List;
import java.util.Locale.Category;
import java.util.Map;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.portfolio.auth.CustomUserDetails;
import com.example.portfolio.dto.UserAddRequest;
import com.example.portfolio.dto.UserProfileEdit;
import com.example.portfolio.dto.UserSkillEdit;
import com.example.portfolio.dto.UserSkillNew;
import com.example.portfolio.entity.Categories;
import com.example.portfolio.entity.LearningData;
import com.example.portfolio.entity.Users;

//データベース操作を行うためのメソッドを入れる
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
    
    CustomUserDetails findById(long id);
    
    public Optional<UserProfileEdit> createUserProfileEdit(long id);
	
    public int updateProfile(UserProfileEdit userProfileEdit);
    
    public List<UserSkillEdit> createUserSkillEdit(long id);
    
    void insertLearningData(UserSkillNew userSkillNew);
    
    public List<UserSkillNew> selectByCategoryId(String categoryId);

    int isDuplicate(UserSkillNew userSkillNew);
}


