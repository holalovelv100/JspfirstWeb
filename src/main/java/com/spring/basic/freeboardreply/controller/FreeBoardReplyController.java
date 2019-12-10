package com.spring.basic.freeboardreply.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.basic.commons.paging.Page;
import com.spring.basic.commons.paging.PageCreator;
import com.spring.basic.freeboardreply.domain.FreeBoardReply;
import com.spring.basic.freeboardreply.service.IFreeBoardReplyService;

@RestController
public class FreeBoardReplyController {

	@Autowired
	private IFreeBoardReplyService replyService;
	
	//コメントリストの照会要請
	@GetMapping("/reply/{boardId}/{page}")
	public Map<String, Object> replyAll(@PathVariable Integer boardId, 
							@PathVariable Integer page) {
		
		Page paging = new Page();
		paging.setPage(page);
		
		Integer replyCnt = replyService.countReplies(boardId);
		PageCreator pc = new PageCreator(paging, replyCnt);
		
		List<FreeBoardReply> replies = replyService.selectAll(boardId, paging);
		
		Map<String, Object> replyMap = new HashMap<>();
		replyMap.put("pc", pc);
		replyMap.put("replies", replies);
		replyMap.put("replyCnt", replyCnt);
		
		
		return replyMap;
	}
	
	
	
	//コメント書くの要請処理
	@PostMapping("/reply")
	public String replyWriter(@RequestBody FreeBoardReply reply) {
		System.out.println("新しいコメントの情報: " + reply);	
		replyService.create(reply);
		
		return "replyRegSuccess";		
	}
	
	//コメント修正の要請処理
	@PutMapping("/reply/{replyId}")
	public String replyModify(@PathVariable("replyId") Integer repId, //アノテーションのロケーションの{replyId}とrepIdが違うから("replyId")で作成。
							@RequestBody FreeBoardReply reply) {  
		
		System.out.println("コメントの修正要請: " + reply);
		
		reply.setReplyId(repId);
		replyService.update(reply);
		
		return "replyModSuccess";
	}
	
	
	//コメント除去の要請処理
	@DeleteMapping("/reply/{replyId}")
	public String replyDelete(@PathVariable Integer replyId, 
									Integer boardId) {
		
		System.out.println("コメント除去の要請: " + replyId + "番");
		replyService.delete(replyId, boardId);
	
		return "replyDelSuccess";
	}
	
	
	
	
	
	
}








