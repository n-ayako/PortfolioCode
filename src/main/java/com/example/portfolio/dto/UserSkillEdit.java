package com.example.portfolio.dto;

import java.io.Serializable;

import lombok.Data;

public class UserSkillEdit {

	private long id;
    private String categoryName;
    private String learningDataName;
    private String studyTime;
    
    // ゲッターとセッター
    public Long getId() {
        return id;
    }

    public void setId(Long userId) {
        this.id = userId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getLearningDataName() {
        return learningDataName;
    }

    public void setLearningDataName(String learningDataName) {
        this.learningDataName = learningDataName;
    }
    
    public String getStudyTime() {
        return studyTime;
    }

    public void setStudyTime(String studyTime) {
        this.studyTime = studyTime;
    }

}




