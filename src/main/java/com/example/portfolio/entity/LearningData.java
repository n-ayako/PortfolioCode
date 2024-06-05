package com.example.portfolio.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "learning_data")
@Data
public class LearningData implements Serializable {
    @Id // 主キーを示すアノテーション
    private Long id;
    private String name;
    private Integer studyTime; // study_timeをマッピング
    private Date month;
    private Integer categoryId; // category_idをマッピング
    private String userId; // user_idをマッピング
    private Date createdAt; // created_atをマッピング
    private Date updatedAt; // updated_atをマッピング
    private List<LearningData> learningList;
}