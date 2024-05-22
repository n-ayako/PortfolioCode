package com.example.portfolio.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;//コントローラーからビューにデータを渡す
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;//HTTPリクエストとコントローラーメソッドをマッピング
import org.springframework.web.bind.annotation.RequestMethod;//列挙型の値を指定することで、特定のHTTPメソッドに対応するリクエストマッピング

import com.example.portfolio.service.UserInfoService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import com.example.portfolio.auth.CustomUserDetails;
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

    // loginにリクエストがあった場合に表示するメソッド
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

	@Autowired
	private UserDetailsService userDetailsService;
    /**
     * ユーザー新規登録
     * @param userRequest リクエストデータ
     * @param model Model
     * @return ポートフォリオ画面
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Validated @ModelAttribute UserAddRequest userRequest, BindingResult result, Model model,
            HttpServletRequest request) {
if (result.hasErrors()) {
            // 入力チェックエラーをおこなう
            List<String> errorList = new ArrayList<String>();
            for (ObjectError error : result.getAllErrors()) {
                errorList.add(error.getDefaultMessage());
            }
            model.addAttribute("validationError", errorList);
            return "/signin";
        }
        // ユーザー情報を保存
        UserInfoService.save(userRequest);
        
        // 登録後の自動ログイン処理
        try {
            // ユーザー情報をロード
            UserDetails userDetails = userDetailsService.loadUserByUsername(userRequest.getEmail());

            // ロードしたユーザー情報をログに出力
            System.out.println("User details: " + userDetails);

            // 認証トークン（ユーザー名、パスワード、およびユーザーの権限情報を保持）の作成
            UsernamePasswordAuthenticationToken authToken = 
                new UsernamePasswordAuthenticationToken(userDetails, userRequest.getPassword(), userDetails.getAuthorities());

            // 作成した認証トークンをログに出力
            System.out.println("Authentication token: " + authToken);

            // セキュリティコンテキストに認証情報を設定
            SecurityContextHolder.getContext().setAuthentication(authToken);

            // 認証情報をセッションに保存
            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        } catch (Exception e) {
            // エラーメッセージをログに出力
            System.out.println("Login error: " + e.getMessage());
            model.addAttribute("loginError", "自動ログインに失敗しました: " + e.getMessage());
            return "/signin";
        }


        // ユーザー情報が登録できたらportfolioにリダイレクト
        return "redirect:/portfolio";
    }

    @GetMapping("/portfolio")
    public String getPortfolioPage(Model model) {
        // 認証情報を取得
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 認証情報からCustomUserDetailsオブジェクトを取得
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        // CustomUserDetailsオブジェクトからnameフィールドの値を取得
        String username = userDetails.getName();
        // モデルにユーザー名を追加
        model.addAttribute("username", username);
        // portfolio.htmlにフォワード
        return "portfolio";
    }
}



