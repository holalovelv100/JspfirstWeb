package com.spring.basic.mvcuser.service;

public interface IOAuthLoginService {
	
	// 소셜로그인 사용시 사용자 정보에 접근하는 Access Token을 발급 받는 메서드
	// socialログインを使う時使用者の情報に接近するAccess Tokenの発給を受けるメソッド。
	String getAccessToken(String authCode) throws Exception;

}
