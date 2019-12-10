package com.spring.basic.freeboard.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.basic.commons.paging.Page;
import com.spring.basic.commons.paging.Search;
import com.spring.basic.freeboard.domain.FreeBoard;
import com.spring.basic.freeboard.repository.IFreeBoardMapper;
import com.spring.basic.freeboardreply.repository.IFreeBoardReplyMapper;

@Service
public class FreeBoardService implements IFreeBoardService {

	@Autowired
	private IFreeBoardReplyMapper replyMapper;
	@Autowired
	private IFreeBoardMapper boardMapper;
	
	@Transactional
	@Override
	public void deleteArticle(Integer boardId) {

		//トランザクション
		replyMapper.deleteAll(boardId);
//		int i = 10 / 0;
		boardMapper.deleteFileNames(boardId);
		boardMapper.delete(boardId);
		
	}

	@Transactional	//insertの繰り返し
	@Override
	public void create(FreeBoard article) {
		boardMapper.create(article);
		
		if(article.getFiles() != null) {
			for (String fileName : article.getFiles()) {
				boardMapper.addFile(fileName);			
			}			
		}
		
	}
	
	@Transactional
	@Override
	public Map<String, Object> selectOne(Integer boardId, Page paging) {
		
		//コントローラに伝達するマップデータ(元のデータ + コメントのリストデータ
		Map<String, Object> returnDatas = new HashMap<>();
		//mapperに伝達するマップデータ(元の書き物の番号 + ページの情報) 
		Map<String, Object> inputDatas = new HashMap<>();
		
		inputDatas.put("boardId", boardId);
		inputDatas.put("page", paging);
		
		returnDatas.put("article", boardMapper.selectOne(boardId));
		returnDatas.put("replies", replyMapper.selectAll(inputDatas));
		
		//照会数の上がり処理
		boardMapper.updateViewCnt(boardId);
		
		return returnDatas;
	}

	@Override
	public List<String> getFileNames(Integer boardId) {
		return boardMapper.getFileNames(boardId);
	}
	
	
	@Override
	public List<FreeBoard> selectAll(Search paging) {
		
		return boardMapper.selectAll(paging);
	}

	@Override
	public Integer countArticles(Search paging) {
		
		return boardMapper.countArticles(paging);
	}

	@Override
	public void update(FreeBoard article) {
		
		boardMapper.update(article);
	}

}
