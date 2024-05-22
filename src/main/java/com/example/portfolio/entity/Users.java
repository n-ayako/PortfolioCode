package com.example.portfolio.entity;

import java.io.Serializable;
import lombok.Data;
import java.util.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
@Data
public class Users implements Serializable {
    @Id // 主キーを示すアノテーション
    private Long id;
    private String email;
    private String name;
    private String selfIntroduction;
    private String password;
    private Date createdAt;
    private Date updatedAt;
    
}

