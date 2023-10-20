-- MEMBER Table Create SQL
-- 테이블 생성 SQL - MEMBER
CREATE TABLE MEMBER
(
    `id`        VARCHAR(255)    NULL        COMMENT '아이디',
    `password`  VARCHAR(255)    NULL        COMMENT '패스워드',
    `name`      VARCHAR(50)     NULL        COMMENT '이름',
    PRIMARY KEY (id)
);