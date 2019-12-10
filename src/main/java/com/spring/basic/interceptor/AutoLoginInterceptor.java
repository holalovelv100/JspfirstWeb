package com.spring.basic.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import com.spring.basic.mvcuser.domain.MvcUser;
import com.spring.basic.mvcuser.service.IMvcUserService;

public class AutoLoginInterceptor extends HandlerInterceptorAdapter implements SessionNames {

	@Autowired
	private IMvcUserService userService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		HttpSession session = request.getSession();
		
		//1. 自動ログインのcookieあるかの可否確認
		Cookie loginCookie =  WebUtils.getCookie(request, "loginCookie");
		if(loginCookie != null) { // 자동로그인을 했다면~
			
			//2. DBでcookieに保存されたSessinId値をもっている会員の情報を照会
			MvcUser user = userService.getUserWithSessionId(loginCookie.getValue());
			if(user != null) {
				session.setAttribute(LOGIN, user);
			}
		}
		
		return true;
	}
}
