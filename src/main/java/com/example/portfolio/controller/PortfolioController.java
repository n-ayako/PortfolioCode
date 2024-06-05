package com.example.portfolio.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.portfolio.service.UserInfoService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import com.example.portfolio.auth.CustomUserDetails;
import com.example.portfolio.dao.UsersMapper;
import com.example.portfolio.dto.UserAddRequest;
import com.example.portfolio.dto.UserProfileEdit;
import com.example.portfolio.dto.UserSkillEdit;
import com.example.portfolio.dto.UserSkillNew;
import com.example.portfolio.dto.UserStudyTimeEdit;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;

/**
 * 
 */
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
            model.addAttribute("loginError", "ログインに失敗しました: " + e.getMessage());
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
    //認証されたユーザーの詳細を取得(CustomUserDetailsには認証されたユーザーの情報が入っている）
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

        //model.addAttribute("notice", "プロフィールを更新しました");
        return "redirect:/portfolio"; // 更新が成功したらportfolioにリダイレクト
    }
    

    //スキル編集ページ
    @GetMapping(value = "/skill_edit")
    public String getUserSkills(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        List<UserSkillEdit> skills = UserInfoService.skillInfo(user);

     // CategoryIdごとにグループ化
        Map<Integer, List<UserSkillEdit>> skillsByCategory = skills.stream()
                .collect(Collectors.groupingBy(UserSkillEdit::getCategoryId));
        
        model.addAttribute("skillsByCategory", skillsByCategory);
        return "skill_edit";
    }


    //学習時間の更新
    @PostMapping("/skill_edit")
    public String editSkill(@ModelAttribute UserStudyTimeEdit userStudyTimeEdit, Model model) {
        try {
            UserInfoService.updateStudyTime(userStudyTimeEdit.getLearningDataId(), userStudyTimeEdit.getStudyTime());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "更新ができませんでした");
            e.printStackTrace();
            return "skill_edit";
        }
        //model.addAttribute("notice", "学習時間を更新しました");
        return "redirect:/skill_edit";
    }

    
    //学習項目追加画面の表示
    @GetMapping("/skill_new")
    public String showNewSkillDataForm(@RequestParam("categoryId") Integer categoryId,
    								   @RequestParam("categoryName") String categoryName,
    								   @AuthenticationPrincipal CustomUserDetails user,
            Model model) {
        // categoryIdをモデルに追加
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categoryName", categoryName);

        // ユーザー情報をモデルに追加
        model.addAttribute("user", user);
        
        // 必要な他のデータをモデルに追加
        model.addAttribute("userSkillNew", new UserSkillNew());

        return "skill_new"; 
    }
    
    //項目の削除処理
    @PostMapping("/skill_delete")
    public String deleteSkill(@RequestParam("id") Long id, Model model) {
        try {
            System.out.println("Deleting learning data ID: " + id);
            UserInfoService.deleteLearningData(id);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "削除ができませんでした");
            e.printStackTrace();
            return "skill_edit";
        }
        //model.addAttribute("notice", "学習項目を削除しました");
        return "redirect:/skill_edit";
    }
    
    //学習項目追加画面での項目追加
    @PostMapping("/save_new_skill")
    public String saveNewSkill(@AuthenticationPrincipal CustomUserDetails user,
            @RequestParam("categoryId") Integer categoryId,
            @RequestParam("categoryName") String categoryName,
            @Validated @ModelAttribute UserSkillNew userSkillNew,
            BindingResult result,
            Model model) {
    	
    	// 月の重複チェックのために現在の日付を取得してuserSkillNewにセットする
    	// 月の情報は登録された際にCURRENT_TIMESTAMPで登録されるので登録されるであろう日付を取得してチェックに利用する
        LocalDate currentMonth = LocalDate.now();
        userSkillNew.setMonth(currentMonth);
    	
        // 項目名と月の重複チェック
        boolean isDuplicate = UserInfoService.isDuplicate(userSkillNew);
        if (isDuplicate) {
            // 重複がある場合の処理
        	//項目名を取得
        	String learningDataName = userSkillNew.getLearningDataName();
        	model.addAttribute("duplicateError", learningDataName + "は既に登録されています");
            model.addAttribute("categoryId", categoryId);
            model.addAttribute("categoryName", categoryName);
            model.addAttribute("learningDataName", learningDataName);
            return "skill_new"; // 入力フォームに戻る
        }
    	
        // バリデーションエラーの数をログに出力
        System.out.println("Errors count: " + result.getErrorCount());
        
        // フィールドごとのエラーメッセージをログに出力
        List<FieldError> fieldErrors = result.getFieldErrors();
        for (FieldError error : fieldErrors) {
            System.out.println("Field: " + error.getField() + ", Message: " + error.getDefaultMessage());
        }

        // 入力チェックエラーの場合
        if (result.hasErrors()) {
            List<String> errorList = new ArrayList<String>();
            for (ObjectError error : result.getAllErrors()) {
                errorList.add(error.getDefaultMessage());
            }
            
            System.out.println("Errors count: " + result.getErrorCount());
            
            model.addAttribute("validationError", errorList);
            model.addAttribute("categoryId", categoryId);
            model.addAttribute("categoryName", categoryName);
            return "skill_new"; // 入力フォームに戻る
        }
        
        
        // ログを出力して重複チェックの実行を確認
        System.out.println("重複チェックが実行されました");
        
        userSkillNew.setUserId(user.getId());
        
        //保存する処理
        UserInfoService.insertLearningData(userSkillNew);

        return "redirect:/skill_edit";
    }

    
}



