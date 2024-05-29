package com.example.portfolio.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;//コントローラーからビューにデータを渡す
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;//HTTPリクエストとコントローラーメソッドをマッピング
import org.springframework.web.bind.annotation.RequestMethod;//列挙型の値を指定することで、特定のHTTPメソッドに対応するリクエストマッピング

import com.example.portfolio.service.UserInfoService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import com.example.portfolio.auth.CustomUserDetails;
import com.example.portfolio.dao.UsersMapper;
import com.example.portfolio.dto.UserAddRequest;
import com.example.portfolio.dto.UserProfileEdit;
import com.example.portfolio.dto.UserSkillEdit;

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
    
    // loginにリクエストがあった場合に表示するメソッド
    @RequestMapping(value = "/profile_edit")
    public String portfolioEdit(Model model) {
        return "profile_edit";
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
            UserDetails 
            userDetails = userDetailsService.loadUserByUsername(userRequest.getEmail());

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
    public String getPortfolioPage(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        // ユーザーの個人情報を取得
    	UserProfileEdit form = UserInfoService.ProfileInfo(user);

        // モデルにユーザー情報と個人情報を追加
        model.addAttribute("customUserDetails", user);
        model.addAttribute("userProfileEdit", form);
        
        // ポートフォリオページを表示
        return "portfolio";
    }
    
    @GetMapping("/profile_edit")
    public String getProfilePage(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        // ユーザーの個人情報を取得
    	UserProfileEdit form = UserInfoService.ProfileInfo(user);

        // モデルにユーザー情報と個人情報を追加
        model.addAttribute("customUserDetails", user);
        model.addAttribute("userProfileEdit", form);
        
        // プロフィール編集ページを表示
        return "profile_edit";
    }

    @PostMapping("/profile_edit")
    public String onProfileEditRequested(@Validated @ModelAttribute UserProfileEdit userProfileEdit,
                                         BindingResult bindingResult,
                                         Model model,
                                         @AuthenticationPrincipal CustomUserDetails user) {
        model.addAttribute("CustomUserDetails", user);

        // 入力チェックエラーをおこなう
        if (bindingResult.hasErrors()) {
            List<String> errorList = new ArrayList<>();
            for (ObjectError error : bindingResult.getAllErrors()) {
                errorList.add(error.getDefaultMessage());
            }
            model.addAttribute("validationError", errorList);
            return "/profile_edit";// バリデーションエラーがあった場合は再度編集フォームを表示
        }

        try {
            UserInfoService.updateProfile(userProfileEdit);//プロフィールを更新
        } catch (Exception e) {
            e.printStackTrace();
            return "/profile_edit"; // 更新中にエラーが発生した場合、再度編集フォームを表示
        }

        model.addAttribute("notice", "処理が正常に完了しました。");
        return "redirect:/portfolio"; // 更新が成功したらportfolioにリダイレクト
    }

    
    @GetMapping(value = "/skill_edit")
    public String getUserSkills(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        List<UserSkillEdit> skills = UserInfoService.skillInfo(user);

        // CategoryNameごとにグループ化
        Map<String, List<UserSkillEdit>> skillsByCategory = skills.stream()
            .collect(Collectors.groupingBy(UserSkillEdit::getCategoryName));
        
        for (UserSkillEdit skill : skills) {
            System.out.println("ID: " + skill.getId());
            System.out.println("カテゴリ: " + skill.getCategoryName());
            System.out.println("カテゴリ: " + skill.getLearningDataName());
            System.out.println("カテゴリ: " + skill.getStudyTime());
        }

        // モデルに追加
        model.addAttribute("skillsByCategory", skillsByCategory);

        return "skill_edit"; // userSkills.htmlというテンプレートを表示
    }

    
    
}



