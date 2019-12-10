package com.spring.basic.freeboard.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.basic.commons.paging.PageCreator;
import com.spring.basic.commons.paging.Search;
import com.spring.basic.freeboard.domain.FreeBoard;
import com.spring.basic.freeboard.service.IFreeBoardService;
import com.spring.basic.freeboardreply.service.IFreeBoardReplyService;

//springのコンテナに該当するコントローラをbeanで登録。
@RestController
public class FreeBoardController {
	

	
	
	@Autowired
	private IFreeBoardService boardService;
	@Autowired
	private IFreeBoardReplyService replyService;
	
	//投稿文のリスト要請、処理
	// # /freeboard/all/14
	// # /freeboard/all/1?keyword=おはよう&condition=content  //内容中「おはよう」というキーワードを見つける。
	@GetMapping("/freeboard/all/{page}")
	public ModelAndView list(@PathVariable Integer page, 
							@ModelAttribute("search") Search paging) {	//Page pagingは自動で新しいオブジェクトを作ってPage.javaのコンストラクタを呼ぶ。
		
		ModelAndView mv = new ModelAndView();
		
		
		paging.setPage(page);
//		page = (page - 1) * 10;		//ページングで処理
		
		List<FreeBoard> articles = boardService.selectAll(paging);
//		System.out.println("###　コントローラがもらったデータのリスト ###");
//		articles.forEach(acs -> System.out.println(acs));
		
		//ページの計算のためなオブジェクト生成
		PageCreator pc = new PageCreator(paging, boardService.countArticles(paging));
		System.out.println("ページングの情報: " + pc);
		
		
		//DBから持ってくるデータを画面へ伝送(returnされると「mv.~」を持っていく)
		mv.addObject("articles", articles);		// keyと value
		mv.addObject("pc", pc);
		
		mv.setViewName("freeboard/list");
		// WEB-INF/views/freeboard/list.jsp
		return mv;
	}
	
	//投稿文の詳細照会について要請処理
	//@ModelAttributeは要請の時伝達されたデータを応答の時、viewページに伝達。
	@GetMapping("/freeboard/{boardId}")	//中括弧　->@PathVariable
	public ModelAndView content(@PathVariable Integer boardId, 
								@ModelAttribute("search") Search paging) { 	// boardId　= {boardId}
								// ("paging")　後の名前が同じであれば作成しなくても済む。
		ModelAndView mv = new ModelAndView();
		
		paging.setCountPerPage(3);	//3つづつ見せられ(固定)。
		Map<String, Object> datas = boardService.selectOne(boardId, paging);
		
		//ページングのアルゴリズムのためなPageCreatorオブジェクト生成
		//PageCreator pc = new PageCreator(paging, replyService.countReplies(boardId));
		
		
		
		mv.addObject("article", datas.get("article"));
		mv.addObject("replies", datas.get("replies"));
//		mv.addObject("paging", paging);
		
		mv.setViewName("freeboard/content");
		return mv;
	}
	
	//投稿文の詳細照会を要請の時、添付ファイルのリストを持ってくる要請の処理 
	@GetMapping("/freeboard/file/{boardId}")
	public ResponseEntity<List<String>> getFileRequest(@PathVariable Integer boardId) {

		try {
			List<String> fileNames = boardService.getFileNames(boardId);
			return new ResponseEntity<>(fileNames, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	//書き物画面の閲覧要請の処理
	@GetMapping("/freeboard")
	public ModelAndView write() {
//		ModelAndView mv = new ModelAndView();
//		mv.setViewName("freeboard/write");
//		return mv;	//一行で~~
		return new ModelAndView("freeboard/write");
		
		
	}
	
	//書き物の要請の処理
//	RedirectAttributes: 再要請の時、再要請のページにデータを伝達するオブジェクト
	@PostMapping("/freeboard")
	public ModelAndView write(FreeBoard article, RedirectAttributes ra) {
		
		System.out.println("クライアントが伝達した新しい投稿データ: " + article);
		System.out.println("クライアントが伝達したファイル名のリスト: " + Arrays.toString(article.getFiles()));
		ModelAndView mv = new ModelAndView();
		
		boardService.create(article);

		ra.addFlashAttribute("message", "regSuccess"); // regSuccess를 message에 담아 list페이지(/freeboard/all/1) 로 들어간다.
		mv.setViewName("redirect:/freeboard/all/1");
		
		return mv;
	}
	
	//投稿文の修正要請の処理// 게시글 수정 요청 처리
	@PostMapping("/freeboard/modify/{boardId}")
	public ModelAndView modifyArticle(@PathVariable Integer boardId
										, FreeBoard article
										, RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		System.out.println("投稿文の修正要請: " + boardId + "번");
		System.out.println("修正の情報: " + article);
		boardService.update(article);
		ra.addFlashAttribute("message", "modSuccess");
		
		
		mv.setViewName("redirect:/freeboard/" + boardId);
		
		return mv;
	}
	
	/*
	// 게시글 삭제 요청 처리
	@PostMapping("/freeboard/{boardId}")
	public ModelAndView replyDelete(@PathVariable Integer boardId) {
		ModelAndView mv = new ModelAndView();
		System.out.println("게시글 삭제 요청: " + boardId + "번");
		boardService.deleteArticle(boardId);

		mv.setViewName("redirect:/freeboard/all/1");
		return mv;
	}
	*/
	
	//投稿文の除去要請の処理
	@DeleteMapping("/freeboard/{boardId}")
	public String deleteArticle(@PathVariable Integer boardId) {

		System.out.println("投稿の除去要請: " + boardId + "番");
		boardService.deleteArticle(boardId);
		
		return "delSuccess";
	}

	
}





