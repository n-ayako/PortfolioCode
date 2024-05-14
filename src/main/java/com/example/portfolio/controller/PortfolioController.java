package com.example.portfolio.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;//コントローラーからビューにデータを渡す
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;//HTTPリクエストとコントローラーメソッドをマッピング
import org.springframework.web.bind.annotation.RequestMethod;//列挙型の値を指定することで、特定のHTTPメソッドに対応するリクエストマッピング
import org.springframework.web.bind.annotation.RequestParam;//HTTPリクエストのパラメーターをコントローラーメソッドの引数にバインド
import org.springframework.web.servlet.ModelAndView;//ントローラーがビューにデータを渡す

import com.example.portfolio.service.UserInfoService;
import com.example.portfolio.dto.UserAddRequest;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;


@Controller
public class PortfolioController {
	//indexへのアクセスがあったらsigninにリダイレクトする
    
    @RequestMapping("/")
    public String showLoginForm(Model model) {
        //サインイン画面へ
        return "redirect:/signin";
    }
    
	
	//クラスのインスタンスを自動的に生成し、他のクラスに依存関係として注入
    @Autowired
    private UserInfoService UserInfoService;
    
    /**
    * ユーザー新規登録画面を表示
    * @param model Model
    * @return 登録画面
    */
    @GetMapping(value = "/signin")
    public String displayAdd(Model model) {
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
    
    // /portfolioリクエストがあった場合にportfolioを表示するメソッド
    @GetMapping("/portfolio")
    public String displayPortfolio(Model model) {
        return "portfolio";
    }
    
    // /loginにリクエストがあった場合にportfolioを表示するメソッド
    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }
    
    
    
}
