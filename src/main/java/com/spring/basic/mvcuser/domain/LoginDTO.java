package com.spring.basic.mvcuser.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class LoginDTO {

	private String account;
	private String password;
	private boolean isAutoLogin; // 自動ログインのチェック可否
	
}
