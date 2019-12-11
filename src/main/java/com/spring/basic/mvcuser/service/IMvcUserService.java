package com.spring.basic.mvcuser.service;

import java.util.Date;

import com.spring.basic.mvcuser.domain.MvcUser;

public interface IMvcUserService {

	//会員登録の機能
	void register(MvcUser user);

	//中腹確認のチェック
	boolean isDuplicate(String kind, String info);

	//会員情報の照会機能
	MvcUser getUserInfo(String account);

	//自動ログインのcookie値をDBに保存する機能
	void keepLogin(String sessionId, Date limitTime, String account);

	//cookie値で会員情報を持ってくる機能
	MvcUser getUserWithSessionId(String sessionId);

	//最終ログインの時間更新
	void updateLastLoginTime(String account);
	
	//카카오로그인한 회원정보 조회
	MvcUser getUserInfoWithKakao(String kakaoAccount);
}

