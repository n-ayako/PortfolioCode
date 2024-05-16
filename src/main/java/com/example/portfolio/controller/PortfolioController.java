package com.example.portfolio.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;//コントローラーからビューにデータを渡す
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;//HTTPリクエストとコントローラーメソッドをマッピング
import org.springframework.web.bind.annotation.RequestMethod;//列挙型の値を指定することで、特定のHTTPメソッドに対応するリクエストマッピング

import com.example.portfolio.service.UserInfoService;
import com.example.portfolio.dto.UserAddRequest;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;


@Controller
public class PortfolioController {
	//ページの表示に関するもの
	//indexへのアクセスがあったらsigninにリダイレクトする
    @RequestMapping("/")
    public String formLogin(Model model) {
        //サインイン画面へ
        return "redirect:/login";
    }
    
    // /portfolioにリクエストがあった場合にportfolioを表示するメソッド
    @RequestMapping(value = "/portfolio")
    public String displayPortfolio(Model model) {
        return "portfolio";
    }

    // /portfolioにリクエストがあった場合にportfolioを表示するメソッド
    @RequestMapping(value = "/login")
    public String displaylogin(Model model) {
        return "login";
    }
	
	//クラスのインスタンスを自動的に生成し、他のクラスに依存関係として注入
    @Autowired
    private UserInfoService UserInfoService;
    
    /**
    * ユーザー新規登録画面に関するもの
    * @param model Model
    * @return 登録画面
    */
    @GetMapping(value = "/signin")
    public String signin(Model model) {
        model.addAttribute("userAddRequest", new UserAddRequest());
        return "/signin";
    }
    
    /**
     * ユーザー新規登録
     * @param userRequest リクエストデータ
     * @param model Model
     * @return ポートフォリオ画面
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Validated @ModelAttribute UserAddRequest userRequest, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // 入力チェックエラーをおこなう
            List<String> errorList = new ArrayList<String>();
            for (ObjectError error : result.getAllErrors()) {
                errorList.add(error.getDefaultMessage());
            }
            model.addAttribute("validationError", errorList);
            return "/signin";
        }
        // ユーザー情報が登録できたらportfolioにリダイレクト
        UserInfoService.save(userRequest);
        return "redirect:/portfolio";
    }
    /*　テストコード
    //ログインしているユーザー名を取得
    @GetMapping("/profile")
    public String showProfile(Model model) {
        // 認証されたユーザーの情報を取得
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // ユーザー名を取得
        String username = authentication.getName();
        
        // ログにユーザー名を出力
        System.out.println("Logged in user: " + username);
        
        // モデルにユーザー名をセットして、Thymeleafで表示
        model.addAttribute("username", "テストユーザー");
        
        return "/portfolio"; // プロフィール表示用のThymeleafテンプレート名を返す
    }
    */
}

