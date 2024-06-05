package com.example.portfolio.service;

import java.util.List;
import java.util.Locale.Category;
import java.util.Map;
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
import com.example.portfolio.dto.UserSkillEdit;
import com.example.portfolio.dto.UserSkillNew;

import com.example.portfolio.entity.LearningData;
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
    
    //プロフィール更新
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
    
	//スキル表示
	public List<UserSkillEdit> skillInfo(CustomUserDetails user) {
	    // ユーザーIdからスキルの情報を取得
	    return usersMapper.createUserSkillEdit(user.getId());
	}
	
	//学習時間表示
    public void updateStudyTime(Long id, int studyTime) {
    	usersMapper.updateStudyTime(id, studyTime);
    }

    //学習項目削除
    public void deleteLearningData(Long id) {
    	usersMapper.deleteLearningData(id);
    }
    
    //項目追加更新
    public void insertLearningData(UserSkillNew userSkillNew) {
        usersMapper.insertLearningData(userSkillNew);
    }
    
    //重複チェック
    public boolean isDuplicate(UserSkillNew userSkillNew) {
        int count = usersMapper.isDuplicate(userSkillNew);
        System.out.println("重複チェックの結果: " + count);
        return count > 0;
    }
    
    
    
    
	
}