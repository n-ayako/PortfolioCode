package com.example.portfolio.dto;

import lombok.Data;

//ゲッターとセッターはLombokの@Dataアノテーションで自動生成さるため記述不要
@Data
public class UserStudyTimeEdit {
    private long learningDataId; // learning_dataテーブルのID
    private int studyTime; // 学習時間
}
