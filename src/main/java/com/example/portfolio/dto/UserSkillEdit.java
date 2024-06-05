package com.example.portfolio.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class UserSkillEdit {
    private long learningDataId;  // learning_dataのID
    private long userId;          // ユーザーのID
    private int categoryId;       // カテゴリのID
    private String categoryName;  // カテゴリの名前
    private String learningDataName; // 学習データの名前
    private int studyTime;        // 学習時間
    private Date month;           // 月
}




