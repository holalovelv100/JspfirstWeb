package com.spring.basic.commons.paging;

import lombok.Getter;
import lombok.Setter;

//  검색기능 + 페이징
@Setter
@Getter
public class Search extends Page {

	private String keyword;
	private String condition;
	
	public Search() {
		this.keyword = "";
		this.condition = "";
	}
}
