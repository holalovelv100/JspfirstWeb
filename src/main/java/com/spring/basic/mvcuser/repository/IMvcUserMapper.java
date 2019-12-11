package com.spring.basic.mvcuser.repository;

import java.util.Map;

import com.spring.basic.mvcuser.domain.MvcUser;

public interface IMvcUserMapper {

	//会員登録の機能
	void register(MvcUser user);
	
	//中腹確認のチェック(ID, Email)
	int isDuplicate(Map<String, Object> datas);
	
	
	//会員情報の照会機能
	MvcUser getUserInfo(String account);
	
	//自動ログインのcookie値をDBに保存する機能
	void keepLogin(Map<String, Object> datas);
	
	//cookie値で会員情報を持ってくる機能
	MvcUser getUserWithSessionId(String sessionId);
	
	//最終ログインの時間更新
	void updateLastLoginTime(String account);
	
	//카카오로그인한 회원정보 조회
	MvcUser getUserInfoWithKakao(String kakaoAccount);
}
