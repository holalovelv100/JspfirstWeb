
-- 댓글 테이블 생성
CREATE TABLE IF NOT EXISTS `spring8`.`free_board_reply` (
  `reply_id` INT NOT NULL AUTO_INCREMENT,
  `reply_text` VARCHAR(1000) NOT NULL,
  `reply_writer` VARCHAR(80) NOT NULL,
  `reg_date` TIMESTAMP NOT NULL DEFAULT NOW(),
  `update_date` TIMESTAMP NOT NULL DEFAULT NOW(),
  `board_id` INT(11) NOT NULL,
  PRIMARY KEY (`reply_id`),
  CONSTRAINT `fk_free_board_reply_free_board`
    FOREIGN KEY (`board_id`)		-- 반드시 원본 게시글을 참조, 외래키
    REFERENCES `spring8`.`free_board` (`board_id`))
ENGINE = InnoDB;


-- 댓글 참조키 설정
ALTER TABLE free_board_reply ADD CONSTRAINT FK_BOARD 
FOREIGN KEY (board_id) REFERENCES free_board (board_id);  -- free_board에 있는 거 참조하겠다.

SELECT * 
FROM free_board_reply; 


TRUNCATE TABLE free_board_reply; 


SELECT * 
FROM free_board_reply  
WHERE board_id=485  
ORDER BY reply_id DESC; 

-- 댓글 삭제 
DELETE FROM free_board_reply 
WHERE reply_id=14; 


SELECT * 
FROM free_board_reply; 
 

-- 페이징 처리 쿼리 
SELECT * 
FROM free_board 
ORDER BY board_id DESC 
LIMIT 0, 10; 

-- 총 게시물 수 구하기  
SELECT COUNT(board_id) 
FROM free_board; 

-- 댓글 페이징 처리 쿼리 
SELECT * 
FROM free_board_reply   
WHERE board_id=484  
LIMIT 0, 3; 


-- 특정 게시물의 총 댓글 수 조회 
SELECT COUNT(*) 
FROM free_board_reply 
WHERE board_id=484   


