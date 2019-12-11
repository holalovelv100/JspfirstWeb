package com.spring.basic.interceptor;

public interface SessionNames {

	//interfaceに変数を宣言すると自動でpublic static finalが付く。
	String LOGIN = "loginUser";
	String ATTEMPTED = "attemptedLocation";
	long LIMIT_TIME  = 60 * 60 * 24 * 90; //一時間＊一日＊90=90日
}
