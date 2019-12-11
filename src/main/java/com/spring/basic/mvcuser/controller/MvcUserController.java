package com.spring.basic.mvcuser.controller;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.spring.basic.interceptor.SessionNames;
import com.spring.basic.mvcuser.domain.LoginDTO;
import com.spring.basic.mvcuser.domain.MvcUser;
import com.spring.basic.mvcuser.service.IMvcUserService;
import com.spring.basic.mvcuser.service.KakaoService;

@RestController
public class MvcUserController {
	
	@Autowired
	private IMvcUserService userService;
	
	@Autowired
	private KakaoService kakaoService;
	
	//会員登録を要請処理するテストメソッド
	@PostMapping("/regTest")
	public String regTest(@RequestBody MvcUser user) {
		System.out.println("param: " + user);
		userService.register(user);
		return "regSuccess";
	}
	
	//データ(account, email)の中腹チェックを要請 
	@GetMapping("/check")
	public ResponseEntity<String> check(String info, String kind) {
		
//		System.out.println("中腹チェックの種類: " + kind);
//		System.out.println("中腹チェックのデータ: " + info);
		
		try {
			boolean flag =userService.isDuplicate(kind, info);
			if(flag) { //中腹データ
				return new ResponseEntity<>("true", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("false", HttpStatus.OK);
			}
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
//	@GetMapping("/register")
//	public ModelAndView register() {
//		ModelAndView mv = new ModelAndView();
//		
//		mv.setViewName("");
//		return mv;
//	}
	
	//会員登録ページ画面の要請処理メソッド
	@GetMapping("/register")
	public ModelAndView registerGET() {
		return new ModelAndView("user/register");		
	}
	
	
	//会員登録の要請処理メソッド//회원등록 요청처리 메서드
	@PostMapping("/register")
	public ModelAndView register(MvcUser regData) {

		ModelAndView mv = new ModelAndView();
		userService.register(regData);
		mv.addObject("regResult", "OK");
		mv.setViewName("user/login");
		return mv;      
	}
	
	//ログインページ画面の要請処理メソッド
	@GetMapping("/login")
	public ModelAndView login(Integer check, HttpSession session) {

		// check => 書き物の番号
		if(check != null)
			session.setAttribute("check", check);
		return new ModelAndView("user/login");
	}
	
	
	//ログイン検証の要請処理メソッド
	@PostMapping("/loginCheck")
	public ModelAndView loginCheck(LoginDTO loginData, HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		
		System.out.println("LoginData: " + loginData);
		ModelAndView mv = new ModelAndView();
		
		MvcUser user = userService.getUserInfo(loginData.getAccount());
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if(user != null) {
			if(encoder.matches(loginData.getPassword(), user.getPassword())) {
				
				//最終ログインの時間更新
				userService.updateLastLoginTime(user.getAccount());
				
				//自動ログインのDB処理
				long expiredDate = System.currentTimeMillis() + SessionNames.LIMIT_TIME * 1000; 
				Date limitDate = new Date(expiredDate);	//DBにはミリ秒ではないデータで入れる
				userService.keepLogin(session.getId(), limitDate, loginData.getAccount());
				
				
				mv.addObject("user", user);
			} else {
				mv.addObject("loginResult", "pwFail");
			}
		} else {
			mv.addObject("loginResult", "idFail");
		}
		mv.setViewName("user/loginCheck");
		return mv;
		
	}
	
	//ログアウトの要請処理
	@GetMapping("/logout")
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		MvcUser user =  (MvcUser)session.getAttribute(SessionNames.LOGIN);

		if(user != null) {

			String loginMethod = (String)session.getAttribute("loginMethod");
			if(loginMethod.equals("kakao")) { //만약 카카오 로그아웃이라면
				kakaoService.kakaoLogout((String)session.getAttribute("access_token"));
			}

			session.removeAttribute(SessionNames.LOGIN);
			session.invalidate();//sessionオブジェクトを無効

			//ログアウトの時、自動ログインcookieを除去および該当会員の情報からsessin_id除去
			/*
			 1. loginCookieを読んだ後、該当cookieが存在するかの可否確認
			 2. cookieが存在するとcookieの寿命を0秒で再び設定(setMaxAge使用)
			 3. 応答オブジェクトを通じてロカールに0秒のcookieを再転送 -> cookie除去
			 4. serviceを通じてkeepLoginを呼び出してDBコラムレコードの再設定
			   (session_id -> "none", limit_time -> 現在時間で)
			 */
			Cookie loginCookie = WebUtils.getCookie(request, "loginCookie");	//getCookie마우스대고 F3/내용을 보면 쿠키 찾게 돌려준다.
			if(loginCookie != null) {
				loginCookie.setMaxAge(0);
				response.addCookie(loginCookie);
				userService.keepLogin("none", new Date(), user.getAccount());
			}			
		}		
		return new ModelAndView("redirect:/");
		
	}
	
}









