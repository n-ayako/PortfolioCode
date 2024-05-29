package com.example.portfolio.entity;

import java.io.Serializable;
import lombok.Data;
import java.util.Date;
import java.util.List;

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
    private String study_time;
    private Date month;
    private Integer category_id;
    private String user_id;
    private Date created_at;
    private Date updated_at;
    private List<LearningData> learningList;
}