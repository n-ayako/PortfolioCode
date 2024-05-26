//Data Transfer Object　データの受け渡しで使用する
package com.example.portfolio.dto;

import java.io.Serializable;//Javaのオブジェクトやデータを別のJavaシステムでも利用できるように、byte形式（stream of bytes）に変換
import lombok.Data;//アノテーションを付けるとetter, setter, toString, equals などの「何度も繰り返し書くコード」をコンパイル時に自動生成

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

//ユーザー情報登録 リクエストデータ
@Data
public class UserAddRequest implements Serializable {
	    //氏名
	    @NotEmpty(message = "※氏名の入力は必須です")
	    @Size(max = 255, message = "氏名は255文字以内で入力してください")
	    private String name;
	    //アドレス
	    @NotEmpty(message = "※メールアドレスの入力は必須です")
	    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "※メールアドレスが正しい形式ではありません")
	    private String email;
	    //パスワード
	    @NotEmpty(message = "※パスワードの入力は必須です")
	    
	    @Pattern(regexp="^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{8,}$", message = "※英数字8文字以上で入力してください")
	    private String password;
	    // dbに初期値が設定できないためバリデーション時にdbに初期値を登録
	    private String selfIntroduction = "デフォルトの自己紹介文";

	    // コンストラクタ
	    public UserAddRequest() {
	        // デフォルト値の設定
	        this.selfIntroduction = "デフォルトの自己紹介文";
	    }
	    public String getSelfIntroduction() {
	        return this.selfIntroduction;
	    }

	}