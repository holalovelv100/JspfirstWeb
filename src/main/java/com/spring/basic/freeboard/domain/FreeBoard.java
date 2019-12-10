package com.spring.basic.freeboard.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//@Data
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FreeBoard {
	
	// camel case : JAVAの名前規則
	// snake case : SQL、Python、通信処理ほうの名前規則(board_id)
	private Integer boardId;
	private String title;
	private String content;
	private String writer;
	private Date regDate;
	private Date updateDate;
	private Integer viewCnt;
	private Integer replyCnt;
	
	//添付ファイルの名前リストを保存するfield
	private String[] files;
	
	
}
