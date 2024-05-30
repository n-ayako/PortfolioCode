package com.example.portfolio.entity;

import java.io.Serializable;
import lombok.Data;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "categories")
@Data
public class Categories implements Serializable {
    @Id // 主キーを示すアノテーション
    private Long id;
    private String name;
    private Date created_at;
    private Date ted_at;
    private List<Categories> categoriesList;
}