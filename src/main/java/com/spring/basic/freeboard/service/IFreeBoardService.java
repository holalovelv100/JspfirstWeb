package com.spring.basic.freeboard.service;

import java.util.List;
import java.util.Map;

import com.spring.basic.commons.paging.Page;
import com.spring.basic.commons.paging.Search;
import com.spring.basic.freeboard.domain.FreeBoard;

public interface IFreeBoardService {

	
	void create(FreeBoard article);
	
	Map<String, Object> selectOne(Integer boardId, Page paging); //照会なら結果が要るのでFreeBoard　タイプ
	List<String> getFileNames(Integer boardId);
	
	List<FreeBoard> selectAll(Search paging);
	Integer countArticles(Search paging);  //総投稿の数を求める

	void update(FreeBoard article);

	
	//投稿の除去についての中間処理メソッド
	void deleteArticle(Integer boardId);
	
	
}
