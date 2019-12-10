SHOW DATABASES; 
USE spring8; 



-- 자유게시판 테이블 생성 
DROP TABLE free_board;  -- 테이블 스키마 삭제 
TRUNCATE TABLE free_board; -- 테이블 레코드(값) 전체 삭제 

CREATE TABLE free_board ( 
	board_id INT PRIMARY KEY AUTO_INCREMENT, 
	title VARCHAR(200) NOT NULL, 
	content TEXT NULL, 
	writer VARCHAR(50) NOT NULL, 
	reg_date TIMESTAMP NOT NULL DEFAULT NOW(), 
	update_date TIMESTAMP NOT NULL DEFAULT NOW(), 
	view_cnt INT DEFAULT 0, 
	reply_cnt INT DEFAULT 0 
); 

SELECT * 
FROM free_board  
ORDER BY board_id DESC; 

SELECT * 
FROM free_board 
WHERE board_id = 100;


 

-- 페이징 처리 쿼리 
SELECT * 
FROM free_board 
ORDER BY board_id DESC 
LIMIT 0, 10; 


