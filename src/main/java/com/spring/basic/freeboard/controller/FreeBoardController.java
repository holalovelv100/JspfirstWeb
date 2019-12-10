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

// 스프링 컨테이너에 해당 컨트롤러를 빈으로 등록
@RestController
public class FreeBoardController {
	

	
	
	@Autowired
	private IFreeBoardService boardService;
	@Autowired
	private IFreeBoardReplyService replyService;
	
	// 게시글 목록 요청에 대한 처리
	// # /freeboard/all/14
	// # /freeboard/all/1?keyword=안녕&condition=content     // 내용에서 '안녕'이란 키워드 찾기.
	@GetMapping("/freeboard/all/{page}")
	public ModelAndView list(@PathVariable Integer page, 
							@ModelAttribute("search") Search paging) {		// Page paging은 자동으로 new 객체를 만들어서.Page.java의 생성자를 부른다.
		
		ModelAndView mv = new ModelAndView();
		
		
		paging.setPage(page);
//		page = (page - 1) * 10;		// 페이징에서 처리하도록 하자.
		
		List<FreeBoard> articles = boardService.selectAll(paging);
//		System.out.println("### 컨트롤러가 받아온 데이터 목록 ###");
//		articles.forEach(acs -> System.out.println(acs));   // 지저분 하니까 주석~
		
		// 페이지 계산을 위한 객체 생성
		PageCreator pc = new PageCreator(paging, boardService.countArticles(paging));
		System.out.println("페이징 정보: " + pc);
		
		
		// DB에서 가져온 데이터 화면으로 전송(리턴되면 mv.을 가지고 간다)
		mv.addObject("articles", articles);		// key와 value값
		mv.addObject("pc", pc);
		
		mv.setViewName("freeboard/list");
		// WEB-INF/views/freeboard/list.jsp
		return mv;
	}
	
	// 게시글 상세조회에 대한 요청 처리
	//@ModelAttribute는 요청시 전달된 데이터를 응답시에 view페이지에 그대로 전달함.
	@GetMapping("/freeboard/{boardId}")		// 중괄호를 사용하기 위해 @PathVariable 를 사용한다.
	public ModelAndView content(@PathVariable Integer boardId, 
								@ModelAttribute("search") Search paging) { 		// boardId는 상단의 {boardId}
								// ("paging") 뒤의 이름이 같으면 작성하지 않아도 된다.
		ModelAndView mv = new ModelAndView();
		
		paging.setCountPerPage(3);		// 3개씩 보여주기 고정으로 한다. 여기주석하고 주소창에 파라미터 countPerPage=숫자를 넣는다면 숫자 만큼 댓글수가 보여진다.
		Map<String, Object> datas = boardService.selectOne(boardId, paging);
		
		// 페이징 알고리즘을 위한 PageCreator객체 생성
		//PageCreator pc = new PageCreator(paging, replyService.countReplies(boardId));
		
		
		
		mv.addObject("article", datas.get("article"));
		mv.addObject("replies", datas.get("replies"));
//		mv.addObject("paging", paging);
		
		mv.setViewName("freeboard/content");
		return mv;
	}
	
	//게시글 상세조회 요청 시 첨부파일 목록을 불러요는 요청 처리 
		@GetMapping("/freeboard/file/{boardId}")
		public ResponseEntity<List<String>> getFileRequest(@PathVariable Integer boardId) {
			
			try {
				List<String> fileNames = boardService.getFileNames(boardId);
				return new ResponseEntity<>(fileNames, HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
	
	
	// 글쓰기 화면 열람 요청 처리
	@GetMapping("/freeboard")
	public ModelAndView write() {
//		ModelAndView mv = new ModelAndView();
//		mv.setViewName("freeboard/write");
//		return mv;	// 한줄로 만들기~
		return new ModelAndView("freeboard/write");
		
		
	}
	
	// 글쓰기 요청 처리
//	RedirectAttributes: 재요청시에 재요청 페이지에 데이터를 전달해주는 객체
	@PostMapping("/freeboard")
	public ModelAndView write(FreeBoard article, RedirectAttributes ra) {
		
		System.out.println("클라이언트가 전달한 신규 게시글 데이터: " + article);
		System.out.println("클라이언트가 전달한 파일명 목록: " + Arrays.toString(article.getFiles()));
		ModelAndView mv = new ModelAndView();
		
		boardService.create(article);

		ra.addFlashAttribute("message", "regSuccess"); // regSuccess를 message에 담아 list페이지(/freeboard/all/1) 로 들어간다.
		mv.setViewName("redirect:/freeboard/all/1");
		
		return mv;
	}
	
	// 게시글 수정 요청 처리
	@PostMapping("/freeboard/modify/{boardId}")
	public ModelAndView modifyArticle(@PathVariable Integer boardId
										, FreeBoard article
										, RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		System.out.println("게시글 수정 요청: " + boardId + "번");
		System.out.println("수정 정보: " + article);
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
	
	// 게시글 삭제 요청 처리
	@DeleteMapping("/freeboard/{boardId}")
	public String deleteArticle(@PathVariable Integer boardId) {

		System.out.println("게시글 삭제 요청: " + boardId + "번");
		boardService.deleteArticle(boardId);
		
		return "delSuccess";
	}

	
}





