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
		
		// 요청객체에서 세션객체 획득
		HttpSession session = request.getSession();
		
		// 로그인이 성공한 이후 처리 로직
		MvcUser user =  (MvcUser)modelAndView.getModel().get("user");
		
		if(user != null) {
			System.out.println("로그인 성공!");
			session.setAttribute(LOGIN, user);
			
			// 자동로그인 처리(null이면 체크 안한거)
			if(request.getParameter("isAutoLogin") != null) {	
				System.out.println("자동 로그인 체크함!!");
				// 자동로그인 쿠키 생성 : 쿠키에 로그인 당시의 고유 세션키(ID)를 저장.
				Cookie loginCookie = new Cookie("loginCookie", session.getId());
				// 쿠키정보 설정
				loginCookie.setPath("/");
				loginCookie.setMaxAge((int)LIMIT_TIME); // 초단위
				
				// 클라이언트에 쿠키 저장.
				response.addCookie(loginCookie);
			}
			
			redirectAttempted(response, session);
			
			
		}
		
	}

	// refactor로 redirectAttempted라는 이름으로 메서드 추출했다. 
	private void redirectAttempted(HttpServletResponse response, HttpSession session) throws IOException {
		// 로그인 전 페이지로 리다이렉트
		String attempted = (String)session.getAttribute(ATTEMPTED);
		if(attempted != null) {
			response.sendRedirect(attempted);	// 리다이렉트 할때는 value
			session.removeAttribute(ATTEMPTED);	// 리무브할때는 상수 key를 받아서
		} else if(session.getAttribute("check") != null) {
			// 댓글 로그인 상황
			response.sendRedirect("/freeboard/" + session.getAttribute("check"));
			session.removeAttribute("check");
		} else {
			response.sendRedirect("/");
		}
	}
	
	
}//end class




