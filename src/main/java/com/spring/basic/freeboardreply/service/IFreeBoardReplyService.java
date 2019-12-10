package com.spring.basic.freeboardreply.service;

import java.util.List;

import com.spring.basic.commons.paging.Page;
import com.spring.basic.freeboardreply.domain.FreeBoardReply;

public interface IFreeBoardReplyService {

	//コメントを書く
	void create(FreeBoardReply reply);

	//コメント修正の機能
	void update(FreeBoardReply reply);

	//コメント除去の機能
	void delete(Integer replyId, Integer boardId);

	//元の投稿を消すと該当投稿のコメントも全部除去
	void deleteAll(Integer boardId);

	//コメントリストの照会機能
	List<FreeBoardReply> selectAll(Integer boardId, Page page);
	
	//特定な投稿の総コメント数を照会する機能
	Integer countReplies(Integer boardId);
	
}
