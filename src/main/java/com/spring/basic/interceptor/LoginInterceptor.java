package com.spring.basic.interceptor;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.spring.basic.mvcuser.domain.MvcUser;

public class LoginInterceptor extends HandlerInterceptorAdapter implements SessionNames {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		HttpSession session = request.getSession();
		if(session.getAttribute(LOGIN) != null)
			session.removeAttribute(LOGIN);
		
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		//要請オブジェクトからセッションオブジェクトを獲得
		HttpSession session = request.getSession();
		
		//ログインが成功した後からの処理
		MvcUser user =  (MvcUser)modelAndView.getModel().get("user");
		
		if(user != null) {
			System.out.println("ログイン成功!");
			session.setAttribute(LOGIN, user);
			
			//自動ログインの処理(null => チェックX)
			if(request.getParameter("isAutoLogin") != null) {	
				System.out.println("자동 로그인 체크함!!");
				//自動ログインのcookie生成: cookieにログインする時の固有なセッションキーを保存
				Cookie loginCookie = new Cookie("loginCookie", session.getId());
				//cookieの情報設定
				loginCookie.setPath("/");
				loginCookie.setMaxAge((int)LIMIT_TIME); //秒単位
				
				//クライアントにcookie保存
				response.addCookie(loginCookie);
			}
			redirectAttempted(response, session);
		}
		
	}

	//refactor、redirectAttemptedという名前でメソッドを抽出 
	private void redirectAttempted(HttpServletResponse response, HttpSession session) throws IOException {
		//ログイン前のページへのredirect
		String attempted = (String)session.getAttribute(ATTEMPTED);
		if(attempted != null) {
			response.sendRedirect(attempted);	//redirectの時はvalue
			session.removeAttribute(ATTEMPTED);	//removeの時は常数keyをもらって
		} else if(session.getAttribute("check") != null) {
			//コメントする時のログイン状況
			response.sendRedirect("/freeboard/" + session.getAttribute("check"));
			session.removeAttribute("check");
		} else {
			response.sendRedirect("/");
		}
	}
		
}//end class




