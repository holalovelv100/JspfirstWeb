package com.spring.basic.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class AuthInterceptor extends HandlerInterceptorAdapter implements SessionNames {

	//会員認証が必要なページに入る時、認証処理するインターセプターの前処理メソッド
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		HttpSession session = request.getSession();
		
		//認証されていない場合にすること
		if(session.getAttribute(LOGIN) == null) {

			String reqUri = request.getRequestURI(); // /freeboard
			String query = request.getQueryString(); // page=4&count=10
			
			if(query != null) {
				reqUri += "?" + query;
			}
			session.setAttribute(ATTEMPTED, reqUri);
			
			response.sendRedirect("/login");
			return false;
		}
		return true;
	}
}
