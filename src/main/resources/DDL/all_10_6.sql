SHOW DATABASES; 
USE spring8; 
SHOW DATABASES; 

-- 자유게시판 테이블 생성 
DROP TABLE free_board;  -- 테이블 스키마 삭제 
TRUNCATE TABLE free_board; -- 테이블 레코드(값) 전체 삭제 

TRUNCATE TABLE free_board_reply; 


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
WHERE board_id > 0 			-- 검색 성능을 높이기위해 작성. 
ORDER BY board_id DESC 
LIMIT 0, 10; 

SELECT COUNT(*)
FROM free_board;

SELECT * 
FROM free_board 
WHERE board_id = 100;

-- 검색 처리 SQL 
SELECT * 
FROM free_board 
WHERE title LIKE '%33%' 		-- 제목은  titile 작성자는 writer로 검색가능 
ORDER BY board_id DESC 
LIMIT 0, 10;


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
    FOREIGN KEY (`board_id`)
    REFERENCES `spring8`.`free_board` (`board_id`))
ENGINE = InnoDB;


SELECT * 
FROM free_board_reply  
WHERE board_id=486  
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
WHERE board_id=484;   

-- 업로드 관련 DB 작성 -- 
-- 파일정보를 가지는 테이블 생성 
CREATE TABLE file_upload ( 
file_name VARCHAR(150) PRIMARY KEY, 
reg_date TIMESTAMP NOT NULL DEFAULT NOW(), 
board_id INT NOT NULL 
);

-- free_board테이블과 연관관계 설정 
ALTER TABLE file_upload 
ADD CONSTRAINT fk_free_board_file_upload 
FOREIGN KEY (board_id) 
REFERENCES free_board (board_id); 

DESC file_upload; 
DESC free_board; 

SELECT * FROM file_upload; 

SELECT file_name 
FROM file_upload 
WHERE board_id=494; 


-- 회원관리 테이블 생성
CREATE TABLE mvc_user (
   account VARCHAR(50) PRIMARY KEY,
    password VARCHAR(150) NOT NULL,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    auth VARCHAR(20) NOT NULL DEFAULT 'common',
    reg_date TIMESTAMP DEFAULT NOW(),
    last_login_at TIMESTAMP NULL
);

DESC mvc_user;

INSERT INTO mvc_user  
(account, password, name, email, auth) 
VALUES ('admin', '1234', '관리자', 'admin@gmail.com', 'admin'); 

INSERT INTO mvc_user  
(account, password, name, email, auth) 
VALUES ('test01', '1234', '테스터01', 'test01@gmail.com', 'test01');

SELECT * 
FROM mvc_user;

-- 아이디 중복확인 
SELECT COUNT(*) 
FROM mvc_user 
WHERE account='test05'; 

-- 자동로그인 관련 컬럼 추가
ALTER TABLE mvc_user ADD COLUMN session_id VARCHAR(80) NOT NULL DEFAULT 'none';
ALTER TABLE mvc_user ADD COLUMN limit_time TIMESTAMP;



-- 카카오 계정 컬럼 추가
ALTER TABLE mvc_user ADD COLUMN kakao_account VARCHAR(80); 

SELECT * FROM mvc_user 
WHERE kakao_account = 'abc1234@nave.com';




TRUNCATE TABLE mvc_user; 



