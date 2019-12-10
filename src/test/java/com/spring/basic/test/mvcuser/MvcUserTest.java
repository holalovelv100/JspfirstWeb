package com.spring.basic.test.mvcuser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.spring.basic.mvcuser.domain.MvcUser;
import com.spring.basic.mvcuser.repository.IMvcUserMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {"classpath:/spring/mvc-config.xml"})
public class MvcUserTest {
	
	@Autowired
	private IMvcUserMapper userMapper;

	@Test
	public void registerTest() {
		
		MvcUser user = new MvcUser();
		user.setAccount("test02");
		user.setPassword("abc1234");
		user.setName("김철수");
		user.setEmail("abc@def.com");
		user.setAuth("common");
		
		userMapper.register(user);
		System.out.println("=====================");
		System.out.println("회원 등록 성공!");
		
	}
	
	@Test
	public void getUserInfoTest() {
		
		String account = "test02";
		MvcUser user = userMapper.getUserInfo(account);
		
		if(user != null) {
			System.out.println("============================");
			System.out.println(user);
			System.out.println("============================");
		}
		
	}
	
	@Test
	public void encodingTest() {
		
		String rawPw = "abc1234";
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPw = encoder.encode(rawPw);
		
		System.out.println("==============================");
		System.out.println("원본 비번: " + rawPw);
		System.out.println("암호화 비번: " + encodedPw);
	}
	
	
	// 로그인테스트
	@Test
	public void loginTest() {
		
		// 로그인 시도 아이디
		String account = "admin";
		// 로그인 시도 패스워드
		String inputPassword = "1234";
		
		
//		String account = "admin";
		MvcUser user =  userMapper.getUserInfo(account);
		
//		System.out.println("==============================");
//		System.out.println("로그인 시도 회원정보: " + user);
//		System.out.println("==============================");
		
		if(user != null) {
			// DB에 저장된 패스워드
			String dbPassword = user.getPassword();
			System.out.println("==============================");
			System.out.println("클라이언트에 입력한 비번: " + inputPassword);
			System.out.println("테이더베이스에 저장된 비번: " + dbPassword);
			
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			
			if(encoder.matches(inputPassword, dbPassword)) {
				// 로그인 서공
				System.out.println("로그인 성공!!");
			} else {
				// 비밀번호 틀림.
				System.out.println("비밀번호가 틀렸습니다.!");
			}
		} else {
			// 회원가입 안된 사용자
			System.out.println("회원가입된 아이디가 아닙니다.");
		}
		
	}
	
	
}












