
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

-- 자동로그인 관련 컬럼 추가
ALTER TABLE mvc_user ADD COLUMN session_id VARCHAR(80) NOT NULL DEFAULT 'none';
ALTER TABLE mvc_user ADD COLUMN limit_time TIMESTAMP;

-- 아이디 중복확인 
SELECT COUNT(*) 
FROM mvc_user 
WHERE account='test05'; 







TRUNCATE TABLE mvc_user; 
