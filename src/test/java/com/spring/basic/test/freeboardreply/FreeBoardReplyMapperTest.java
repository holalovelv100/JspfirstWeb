package com.spring.basic.test.freeboardreply;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.spring.basic.freeboard.domain.FreeBoard;
import com.spring.basic.freeboardreply.domain.FreeBoardReply;
import com.spring.basic.freeboardreply.repository.IFreeBoardReplyMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {"classpath:/spring/mvc-config.xml"})
public class FreeBoardReplyMapperTest {
	
	@Autowired
	private IFreeBoardReplyMapper mapper;
	
	@Test
	public void createTest() {
		
		FreeBoardReply reply = new FreeBoardReply();
		reply.setBoardId(100);
		reply.setReplyText("100번에 글에 대한 두번째 댓글 입니다.");
		reply.setReplyWriter("댓글러");
		
		mapper.create(reply);
		System.out.println("댓글 등록 완료!");
	}

	
	@Test
	public void updateTest() {
		
		FreeBoardReply reply = new FreeBoardReply();
		reply.setReplyText("수정 합니다~~~~");
		reply.setReplyId(3);
		mapper.update(reply);
		System.out.println("댓글 수정 완료!");	
	}
	
	@Test
	public void deleteTest() {
		mapper.delete(4);
		System.out.println("댓글 삭제 완료!!");
	}
	
	@Test
	public void selectAllTest() {
		System.out.println("============ 댓글 전체 조회 입니다. =============");
		/*
		List<FreeBoardReply> replys = mapper.selectAll(99);
		System.out.println("댓글 수: " + replys.size());
		for (FreeBoardReply freeBoardReply : replys) {
			System.out.println(freeBoardReply);
		}
		*/
		System.out.println("=========================================");
	}
	
	/*
	@Test
	public void selectAllTest() {
		System.out.println("============ 댓글 전체 조회 입니다. =============");
		
//		List<FreeBoardReply> replylist = mapper.selectAll(99);
//		for (FreeBoardReply re : replylist) {
//			System.out.println(re);
//		}
		// for문을 람다식으로
		mapper.selectAll(99).forEach(re -> System.out.println(re));
		System.out.println("=========================================");
	}
	*/
}








