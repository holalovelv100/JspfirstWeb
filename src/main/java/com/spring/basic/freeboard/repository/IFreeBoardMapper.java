package com.spring.basic.freeboard.repository;

import java.util.List;
import java.util.Map;

import com.spring.basic.commons.paging.Search;
import com.spring.basic.freeboard.domain.FreeBoard;

public interface IFreeBoardMapper {
	
	// CRUD관련 추상 메서드 선언.
	// 1. create : 글 쓰기 기능.
	void create(FreeBoard article);
	// 1-b. addFile: 파일 첨부 기능
	void addFile(String fileName);

	// 2. read : 조회 기능(
	//	- a: single row -> 특정 게시글 조회
	//	- b: multi row -> 게시글 목록 조회
	FreeBoard selectOne(Integer boardId); // 조회라면 결과가 있어야 하니까 FreeBoard 타입
	List<String> getFileNames(Integer boardId);
	
	List<FreeBoard> selectAll(Search paging);
	
	Integer countArticles(Search paging);  // 총 게시물 수 구하기
	
	// 3. update: 글 수정 기능
	void update(FreeBoard article);	// FreeBoard객체가 가지고있다. article이름은 내맘대로
	
	// 댓글개수 상승, 하락 처리
	void updateReplyCnt(Map<String, Object> datas);		
	// Integer count, Integer boardId 두개를 넣기에는....묶어서 보내기위해 클래스를 또 만들기엔.... 그냥 맵을 이용하기~
	// 댓글 인서트 하고 댓글 등록버튼 클릭시 요청이 발생하고 댓글 쓰기 요청 처리
	
	// 조회수 상승 처리
	void updateViewCnt(Integer boardId);
	
	// 4. delete : 글 삭제 기능
	void delete(Integer boardId);
	
	void deleteFileNames(Integer boardId);
	
	
	
	
	
}


