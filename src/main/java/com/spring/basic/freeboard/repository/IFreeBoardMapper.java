package com.spring.basic.freeboard.repository;

import java.util.List;
import java.util.Map;

import com.spring.basic.commons.paging.Search;
import com.spring.basic.freeboard.domain.FreeBoard;

public interface IFreeBoardMapper {
	
	//CRUD関連の抽象メソッドの宣言
	// 1. create : 書き物書く機能
	void create(FreeBoard article);
	// 1-b. addFile: ファイルの添付機能
	void addFile(String fileName);

	// 2. read : 照会機能
	//	- a: single row -> 特定な投稿の照会
	//	- b: multi row -> 投稿リストの照会
	FreeBoard selectOne(Integer boardId); //照会なら結果が要るのでFreeBoard　タイプ
	List<String> getFileNames(Integer boardId);
	
	List<FreeBoard> selectAll(Search paging);
	
	Integer countArticles(Search paging);  //総投稿の数を求める
	
	// 3. update: 書き物の修正機能
	void update(FreeBoard article);	// FreeBoardオブジェクトが持っている
	
	//コメントの上がり下がりの処理
	void updateReplyCnt(Map<String, Object> datas);		
	// Integer count、Integer boardId　マップを使用
	//コメントをinsertしてコメントの登録ボタンをクリックする時、要請が発生してコメントを書く要請の処理
	
	//照会数の上がり処理
	void updateViewCnt(Integer boardId);
	
	// 4. delete : 書き物の除去機能
	void delete(Integer boardId);
	
	void deleteFileNames(Integer boardId);
	
	
	
	
	
}


