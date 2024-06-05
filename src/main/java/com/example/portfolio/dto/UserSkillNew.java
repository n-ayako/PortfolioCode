package com.example.portfolio.dto;

import java.time.LocalDate;
import java.util.Date;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

//ゲッターとセッターはLombokの@Dataアノテーションで自動生成さるため記述不要
@Data
public class UserSkillNew {
    private Long id;
    private Integer categoryId;
    private String categoryName;
    private Long userId;
	private LocalDate month;

	@NotEmpty(message = "項目名は必ず入力してください")
    @Size(max = 50,message ="項目名は50文字以内で入力してください")
    private String learningDataName;

	@NotNull(message = "学習時間は必ず入力してください")
    @Min(value = 1, message = "学習時間は1以上の数字で入力してください")
    private Integer studyTime;
}
