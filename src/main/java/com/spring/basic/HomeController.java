package com.spring.basic;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Handles requests for the application home page.
 */
@Controller		// 요청을 받아주고 응답한다.
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
//	@RequestMapping(value = "/", method = RequestMethod.GET)	// 3버전 // GET이면 GET POST면 POST
	@GetMapping("/")						// 4버전 // GET이면 GET POST면 POST	(어떤 URL의 어떤것을 매핑할지)
//	@ResponseBody			// 있고 없고의 차이는??
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);		// 로그를 찍어준다.슬러시요청(localhost/)에 응답한다.
		
		Date date = new Date();		// 현재시간을 아래 포매팅 해서  속성에 등록(addAtribute)하여 serverTime이랑 보내고 있다.
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );		// serverTime이란 변수에 formattedDate담아 보낸다.
		model.addAttribute("greeting", "bye bye~~!!");
		
		return "home";	// viewResolver에게 보냈을 때 완성되는 문자열: /WEB-INF/views/home.jsp
	}
	
	@GetMapping("/test/rep")
	public String testReply() {
		return "test/reply-test";
	}
	
}









