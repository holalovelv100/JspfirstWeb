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