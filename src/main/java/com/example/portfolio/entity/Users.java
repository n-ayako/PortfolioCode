package com.example.portfolio.entity;

import java.io.Serializable;//Javaのオブジェクトやデータを別のJavaシステムでも利用できるように、byte形式（stream of bytes）に変換
import lombok.Data;//アノテーションを付けるとetter, setter, toString, equals などの「何度も繰り返し書くコード」をコンパイル時に自動生成
import java.util.Date;//日時を取得したり、日時を計算するときに使用する日付型のクラス

//ユーザー情報 Entity（データベースのテーブル構造を表したオブジェクト）
@Data
public class Users implements Serializable{
	//ID
	private Long id;
	
	//メールアドレス
	private String email;
	
	//氏名
	private String name;
	
	//自己紹介文
	private String selfIntroduction;
	
	//パスワード
	private String password;
	
	//作成日
	private Date createdAt;
	
	//更新日
	private Date updatedAt;
}
