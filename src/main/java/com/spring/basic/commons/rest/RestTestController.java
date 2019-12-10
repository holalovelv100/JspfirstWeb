package com.spring.basic.commons.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.basic.freeboard.domain.FreeBoard;

@RestController
public class RestTestController {

	@GetMapping("/rest/hello")
	//returnデータをViewResolverに伝達しないで、すぐクライアントへ伝送
	public String hello() {
		return "Hello world!!!";
	}
	
	@GetMapping("/rest/obj")
	public FreeBoard testObj() {
		FreeBoard article = new FreeBoard();
		
		article.setBoardId(99);
		article.setTitle("JSON 伝送テスト");
		article.setContent("RestController作動中");
		article.setWriter("a");
		
		return article;
	}
	
	@GetMapping("/rest/list")
	public List<String> testList() {
		List<String> list = new ArrayList<>();
		list.add("ぼのぼの");list.add("シマリスくん");list.add("アライグマくん");
		return list;
	}

	@GetMapping("/rest/map")
	public Map<String, Object> testMap() {
		Map<String, Object> map = new HashMap<>();
		
		map.put("名前", "田中");
		map.put("年齢", 34);
		map.put("趣味", new String[] {"サッカー", "ゲーム", "音楽鑑賞"});
		
		return map;
	}
	
}











