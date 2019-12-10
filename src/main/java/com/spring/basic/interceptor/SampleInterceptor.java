package com.spring.basic.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.spring.basic.freeboard.domain.FreeBoard;

// 인터셉터를 구현하려면 아래의 클래스를 상속받습니다.
public class SampleInterceptor extends HandlerInterceptorAdapter {

	// 컨트롤러에 진입하기 전에 공통처리해야할 내용들에 대한 기능 정의
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		System.out.println("preHandle 작동!!");
		System.out.println("요청 URI: " + request.getRequestURI());
		System.out.println("handler: " + handler);	// 핸들러는 작동한 메서드의 (컨트롤러 안의 메서드)정보를 보여줌.
		return true;
	}
	
	// 컨트롤러의 로직 수행 후 컨트롤러를 빠져나갈 때 공통처리해야 할 내용들에 대한 기능 정의
	@SuppressWarnings("unchecked") // 아래 작성한 list 노란줄 때문에 작성하였으나 크게 중요하지는 않은 아노테이션.
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		System.out.println("postHandle 작동!!");
		// page < request(model) < session < application
		List<FreeBoard> list = (List<FreeBoard>)modelAndView.getModel().get("articles"); 
		// mv.addObject("articles", articles) FreeBoardController에서의 key값 articles
		System.out.println("==========================================");
		list.forEach(b -> System.out.println(b));
		System.out.println("==========================================");
		
	}

}
