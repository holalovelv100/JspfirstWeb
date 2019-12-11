package com.spring.basic.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.spring.basic.freeboard.domain.FreeBoard;

//interceptorの具現するために下のクラス継承(けいしょう)
public class SampleInterceptor extends HandlerInterceptorAdapter {

	//コントローラに入る前に通有の処理すべきの内容について機能定義
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		System.out.println("preHandle 作動!!");
		System.out.println("요청 URI: " + request.getRequestURI());
		System.out.println("handler: " + handler);	//handlerは作動したメソッド(コントローラ中のメソッド)の情報を見せる。
		return true;
	}
	
	//コントローラのロジックを実行した後、コントローラから出る時、通有の処理すべきの内容について機能定義
	@SuppressWarnings("unchecked") //別に　//아래 작성한 list 노란줄 때문에 작성하였으나 크게 중요하지는 않은 아노테이션.
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		System.out.println("postHandle 作動!!");
		// page < request(model) < session < application
		List<FreeBoard> list = (List<FreeBoard>)modelAndView.getModel().get("articles"); 
		// mv.addObject("articles", articles) FreeBoardControllerのkey値:articles
		System.out.println("==========================================");
		list.forEach(b -> System.out.println(b));
		System.out.println("==========================================");
		
	}

}
