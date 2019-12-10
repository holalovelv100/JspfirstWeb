package com.spring.basic.commons.paging;

import lombok.Getter;

@Getter
public class Page {

	private Integer page; //クライアントが要請したページの情報
	private Integer countPerPage; //クライアントが要請した一つのページからみせる掲示物の数
	
	
	public Page() { //コンストラクタで初期化
		this.page = 1;
		this.countPerPage = 10;	
	}
	//ページの情報をDBへ伝達する時、再計算するメソッド宣言。
	public Integer getPageStart() {
		return (page - 1) * countPerPage;
	}
	
	
	public void setPage(Integer page) {
		if(page <= 0 ) {
			this.page = 1;
			return;
		}
		this.page = page;
	}
	
	public void setCountPerPage(Integer countPerPage) {
		if(countPerPage > 100 || countPerPage <= 0) {
			this.countPerPage = 10;
			return;
		}
		this.countPerPage = countPerPage;
	}
	
}








