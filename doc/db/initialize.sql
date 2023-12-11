-- 데이터베이스 선택
USE jhonlinejudge;
-- 데이터베이스 선택
USE jhonlinejudge;

DROP TABLE IF EXISTS BoardPost;
DROP TABLE IF EXISTS Submission;
DROP TABLE IF EXISTS DisplayTestCase;
DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS InternalTestCase;
DROP TABLE IF EXISTS ProblemAlgorithmCategory;
DROP TABLE IF EXISTS AlgorithmCategory;
DROP TABLE IF EXISTS Problem;
DROP TABLE IF EXISTS Level;

-- Level 테이블 생성
CREATE TABLE Level (
    level_id INT PRIMARY KEY AUTO_INCREMENT,
    level VARCHAR(20) NOT NULL
);

-- AlgorithmCategory 테이블 생성
CREATE TABLE AlgorithmCategory (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(50) NOT NULL
);

-- Problem 테이블 생성
CREATE TABLE Problem (
    problem_id INT PRIMARY KEY AUTO_INCREMENT,
    problem_name VARCHAR(100) NOT NULL,
    level_id INT,
    solved_count INT DEFAULT 0,
    wrong_count INT DEFAULT 0,
    FOREIGN KEY (level_id) REFERENCES Level(level_id)
);

-- ProblemAlgorithmCategory 테이블 생성
CREATE TABLE ProblemAlgorithmCategory (
    problem_id INT,
    category_id INT,
    PRIMARY KEY (problem_id, category_id),
    FOREIGN KEY (problem_id) REFERENCES Problem(problem_id),
    FOREIGN KEY (category_id) REFERENCES AlgorithmCategory(category_id)
);

-- InternalTestCase 테이블 생성 (내부 테스트 케이스)
CREATE TABLE InternalTestCase (
    problem_id INT,
    input_case TEXT,
    output_case TEXT,
    PRIMARY KEY (problem_id),
    FOREIGN KEY (problem_id) REFERENCES Problem(problem_id)
);

-- User 테이블 생성
CREATE TABLE User (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(50) NOT NULL,
    level_id INT NOT NULL,
    FOREIGN KEY (level_id) REFERENCES Level(level_id)
);

-- DisplayTestCase 테이블 생성 (사용자에게 보여지는 테스트 케이스)
CREATE TABLE DisplayTestCase (
    problem_id INT,
    input_case TEXT,
    output_case TEXT,
    PRIMARY KEY (problem_id),
    FOREIGN KEY (problem_id) REFERENCES Problem(problem_id)
);

-- Submission 테이블 생성
CREATE TABLE Submission (
    submission_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    problem_id INT,
    code TEXT,
    result BOOLEAN,
    submission_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES User(id),
    FOREIGN KEY (problem_id) REFERENCES Problem(problem_id)
);

-- BoardPost 테이블 생성
CREATE TABLE BoardPost (
    post_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES User(id)
);
