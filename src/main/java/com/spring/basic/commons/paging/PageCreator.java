package com.spring.basic.commons.paging;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PageCreator {

   //ページ番号の情報と1ページ当たり、入る掲示物数の情報を持ってるオブジェクト
   private Page paging;
   private Integer articleTotalCount;//掲示板の総掲示物の数
   private Integer beginPage; //スタートページ番号
   private Integer endPage; //ラストページ番号
   private boolean prev; //以前ボタン、活性化の可否
   private boolean next; //次のボタン、活性化の可否
   
   //１画面に限るページの数
   private final Integer displayPageNum = 10;

   public PageCreator(Page paging, Integer articleTotalCount) {
      this.paging = paging;
      this.articleTotalCount = articleTotalCount;

      //補正する前にラストページ取得
      endPage = (int)Math.ceil(paging.getPage() 
            / (double)displayPageNum)
            * displayPageNum;

      //スタートページの番号取得
      beginPage = (endPage - displayPageNum) + 1;

      //スタートページが１なら以前ボタンの活性化の可否がfalse
      prev = (beginPage == 1) ? false : true;

      //ラストページかの可否を確かめて次のボタン、非活性。
      next = (articleTotalCount <= (endPage * paging.getCountPerPage())) ? false : true;

      //再補正の可否判断
      if(!isNext()) {
         //ラストページ、再補正
         endPage = (int)Math.ceil(articleTotalCount / (double)paging.getCountPerPage());
      }

   }

}








