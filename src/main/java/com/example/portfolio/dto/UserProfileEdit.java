package com.example.portfolio.dto;

import java.io.Serializable;


//バリデーションはjakartaを使用する
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

//ログイン後のユーザー情報取得に使用
@Data
public class UserProfileEdit implements Serializable{
	
	private long id;
	
	@Size(min = 50, max = 200, message = "50文字以上、200文字以下で入力してください")
	private String selfIntroduction;

	private String name;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getSelfIntroduction() {
	    return selfIntroduction;
	}

	public void setSelfIntroduction(String selfIntroduction) {
	    this.selfIntroduction = selfIntroduction;
	}
	
	public String getName() {
	    return name;
	}

	public void getName(String name) {
	    this.name = name;
	}
}

