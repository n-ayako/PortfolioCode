package com.example.portfolio.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale.Category;

import lombok.Data;


public class UserSkillEdit {

	private long id;
	private int categoryId;
    private String categoryName;
    private String learningDataName;
    private int studyTime;
	private Date month;
    
    // ゲッターとセッター
    public Long getId() {
        return id;
    }

    public void setId(Long userId) {
        this.id = userId;
    }
    
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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
    
    public int getStudyTime() {
        return studyTime;
    }

    public void setStudyTime(int studyTime) {
        this.studyTime = studyTime;
    }
    
    public Date getMonth() {
        return month;
    }

    public void setMonth(Date month) {
        this.month = month;
    }

}



