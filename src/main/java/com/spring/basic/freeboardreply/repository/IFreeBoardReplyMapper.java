package com.spring.basic.freeboardreply.repository;

import java.util.List;
import java.util.Map;

import com.spring.basic.freeboardreply.domain.FreeBoardReply;

public interface IFreeBoardReplyMapper {

	//コメントを書く
	void create(FreeBoardReply reply);
	
	//コメント修正の機能
	void update(FreeBoardReply reply);
	
	//コメント除去の機能
	void delete(Integer replyId);
	
	//元の投稿を消すと該当投稿のコメントも全部除去
	void deleteAll(Integer boardId);
	
	//コメントリストの照会機能
	List<FreeBoardReply> selectAll(Map<String, Object> datas);
	
	//特定な投稿の総コメント数を照会する機能
	Integer countReplies(Integer boardId);
}
