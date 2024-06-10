package com.example.portfolio.dto;

import java.math.BigDecimal;
import java.sql.Date;

import lombok.Data;

@Data
public class UserStudyTimeSum {

    private Long categoryId;
    private String categoryName;
    private String month;
    private BigDecimal totalStudyTime;
	
}
