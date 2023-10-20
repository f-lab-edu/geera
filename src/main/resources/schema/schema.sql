-- 테이블 순서는 관계를 고려하여 한 번에 실행해도 에러가 발생하지 않게 정렬되었습니다.

-- PROJECT Table Create SQL
-- 테이블 생성 SQL - PROJECT
CREATE TABLE PROJECT
(
    `project_id`    BIGINT          NOT NULL    AUTO_INCREMENT COMMENT '프로젝트 ID',
    `project_name`  VARCHAR(255)    NULL        COMMENT '프로젝트명',
    `start_date`    TIMESTAMP       NULL        COMMENT '프로젝트 생성일',
    PRIMARY KEY (project_id)
);

-- 테이블 Comment 설정 SQL - PROJECT
ALTER TABLE PROJECT COMMENT '프로젝트';


-- SPRINT Table Create SQL
-- 테이블 생성 SQL - SPRINT
CREATE TABLE SPRINT
(
    `sprint_id`          BIGINT          NOT NULL    AUTO_INCREMENT COMMENT '스프린트 ID',
    `project_id`         BIGINT          NULL        COMMENT '프로젝트 ID',
    `sprint_name`        VARCHAR(255)    NULL        COMMENT '스프린트 이름',
    `sprint_start_date`  DATE            NULL        COMMENT '시작일',
    `sprint_end_date`    DATE            NULL        COMMENT '종료일',
    `sprint_status`      TINYINT(1)      NULL        COMMENT '진행 상태',
    PRIMARY KEY (sprint_id)
);

-- Foreign Key 설정 SQL - SPRINT(project_id) -> PROJECT(project_id)
ALTER TABLE SPRINT
    ADD CONSTRAINT FK_SPRINT_project_id_PROJECT_project_id FOREIGN KEY (project_id)
        REFERENCES PROJECT (project_id) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - SPRINT(project_id)
-- ALTER TABLE SPRINT
-- DROP FOREIGN KEY FK_SPRINT_project_id_PROJECT_project_id;


-- MEMBER Table Create SQL
-- 테이블 생성 SQL - MEMBER
CREATE TABLE MEMBER
(
    `id`        VARCHAR(255)    NULL        COMMENT '아이디',
    `password`  VARCHAR(255)    NULL        COMMENT '패스워드',
    `name`      VARCHAR(50)     NULL        COMMENT '이름',
    PRIMARY KEY (id)
);

-- 테이블 Comment 설정 SQL - MEMBER
ALTER TABLE MEMBER COMMENT '사용자';


-- ISSUE Table Create SQL
-- 테이블 생성 SQL - ISSUE
CREATE TABLE ISSUE
(
    `issue_id`           BIGINT          NOT NULL    AUTO_INCREMENT COMMENT '이슈 ID',
    `project_id`         BIGINT          NULL        COMMENT '프로젝트 ID',
    `issue_type`         VARCHAR(45)     NULL        COMMENT '이슈 유형',
    `issue_status`       VARCHAR(45)     NULL        COMMENT '상태',
    `epic_id`            BIGINT          NOT NULL    COMMENT '에픽 ID',
    `issue_description`  VARCHAR(255)    NOT NULL    COMMENT '요약',
    `issue_detail`       TEXT            NOT NULL    COMMENT '상세 설명',
    `issue_contract_id`  VARCHAR(255)    NOT NULL    COMMENT '담당자 ID',
    `issue_reporter_id`  VARCHAR(255)    NOT NULL    COMMENT '보고자 ID',
    `issue_priority`     INT             NOT NULL    COMMENT '우선 순위',
    `sprint_id`          BIGINT          NOT NULL    COMMENT '스프린트 ID',
    `create_at`          TIMESTAMP       NOT NULL    COMMENT '생성일',
    `top_issue`          BIGINT          NULL        COMMENT '상위이슈',
    PRIMARY KEY (issue_id)
);

-- Foreign Key 설정 SQL - ISSUE(project_id) -> PROJECT(project_id)
ALTER TABLE ISSUE
    ADD CONSTRAINT FK_ISSUE_project_id_PROJECT_project_id FOREIGN KEY (project_id)
        REFERENCES PROJECT (project_id) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - ISSUE(project_id)
-- ALTER TABLE ISSUE
-- DROP FOREIGN KEY FK_ISSUE_project_id_PROJECT_project_id;

-- Foreign Key 설정 SQL - ISSUE(sprint_id) -> SPRINT(sprint_id)
ALTER TABLE ISSUE
    ADD CONSTRAINT FK_ISSUE_sprint_id_SPRINT_sprint_id FOREIGN KEY (sprint_id)
        REFERENCES SPRINT (sprint_id) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - ISSUE(sprint_id)
-- ALTER TABLE ISSUE
-- DROP FOREIGN KEY FK_ISSUE_sprint_id_SPRINT_sprint_id;

-- Foreign Key 설정 SQL - ISSUE(issue_contract_id) -> MEMBER(id)
ALTER TABLE ISSUE
    ADD CONSTRAINT FK_ISSUE_issue_contract_id_MEMBER_id FOREIGN KEY (issue_contract_id)
        REFERENCES MEMBER (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - ISSUE(issue_contract_id)
-- ALTER TABLE ISSUE
-- DROP FOREIGN KEY FK_ISSUE_issue_contract_id_MEMBER_id;

-- Foreign Key 설정 SQL - ISSUE(issue_reporter_id) -> MEMBER(id)
ALTER TABLE ISSUE
    ADD CONSTRAINT FK_ISSUE_issue_reporter_id_MEMBER_id FOREIGN KEY (issue_reporter_id)
        REFERENCES MEMBER (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - ISSUE(issue_reporter_id)
-- ALTER TABLE ISSUE
-- DROP FOREIGN KEY FK_ISSUE_issue_reporter_id_MEMBER_id;


-- ISSUE_COMMENT Table Create SQL
-- 테이블 생성 SQL - ISSUE_COMMENT
CREATE TABLE ISSUE_COMMENT
(
    `issue_comment_id`       BIGINT          NOT NULL    AUTO_INCREMENT COMMENT '코멘트 ID',
    `member_id`              VARCHAR(255)    NULL        COMMENT '멤버 아이디',
    `issue_id`               BIGINT          NULL        COMMENT '이슈 ID',
    `Issue_comment_content`  TEXT            NULL        COMMENT '코멘트 내용',
    PRIMARY KEY (issue_comment_id)
);

-- Foreign Key 설정 SQL - ISSUE_COMMENT(member_id) -> MEMBER(id)
ALTER TABLE ISSUE_COMMENT
    ADD CONSTRAINT FK_ISSUE_COMMENT_member_id_MEMBER_id FOREIGN KEY (member_id)
        REFERENCES MEMBER (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - ISSUE_COMMENT(member_id)
-- ALTER TABLE ISSUE_COMMENT
-- DROP FOREIGN KEY FK_ISSUE_COMMENT_member_id_MEMBER_id;


-- ISSUE_SUBSCRIBE Table Create SQL
-- 테이블 생성 SQL - ISSUE_SUBSCRIBE
CREATE TABLE ISSUE_SUBSCRIBE
(
    `issue_subscribe_id`  int             NOT NULL    COMMENT '구독 ID',
    `user_id`             varchar(255)    NOT NULL    COMMENT '회원 ID',
    `issue_id`            BIGINT          NOT NULL    COMMENT '이슈 ID',
    PRIMARY KEY (issue_subscribe_id)
);

-- Foreign Key 설정 SQL - ISSUE_SUBSCRIBE(user_id) -> MEMBER(id)
ALTER TABLE ISSUE_SUBSCRIBE
    ADD CONSTRAINT FK_ISSUE_SUBSCRIBE_user_id_MEMBER_id FOREIGN KEY (user_id)
        REFERENCES MEMBER (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - ISSUE_SUBSCRIBE(user_id)
-- ALTER TABLE ISSUE_SUBSCRIBE
-- DROP FOREIGN KEY FK_ISSUE_SUBSCRIBE_user_id_MEMBER_id;

-- Foreign Key 설정 SQL - ISSUE_SUBSCRIBE(issue_id) -> ISSUE(issue_id)
ALTER TABLE ISSUE_SUBSCRIBE
    ADD CONSTRAINT FK_ISSUE_SUBSCRIBE_issue_id_ISSUE_issue_id FOREIGN KEY (issue_id)
        REFERENCES ISSUE (issue_id) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - ISSUE_SUBSCRIBE(issue_id)
-- ALTER TABLE ISSUE_SUBSCRIBE
-- DROP FOREIGN KEY FK_ISSUE_SUBSCRIBE_issue_id_ISSUE_issue_id;


-- ISSUE_UPDATR_LOG Table Create SQL
-- 테이블 생성 SQL - ISSUE_UPDATR_LOG
CREATE TABLE ISSUE_UPDATR_LOG
(
    `log_id`              BIGINT          NOT NULL    AUTO_INCREMENT COMMENT '로그 ID',
    `issue_id`            BIGINT          NULL        COMMENT '이슈 ID',
    `updated_by`          VARCHAR(255)    NULL        COMMENT '업데이트 맴버 ID',
    `update_description`  TEXT            NULL        COMMENT '업데이트 내용',
    `update_at`           TIMESTAMP       NULL        COMMENT '업데이트 시간',
    PRIMARY KEY (log_id)
);

-- Foreign Key 설정 SQL - ISSUE_UPDATR_LOG(issue_id) -> ISSUE(issue_id)
ALTER TABLE ISSUE_UPDATR_LOG
    ADD CONSTRAINT FK_ISSUE_UPDATR_LOG_issue_id_ISSUE_issue_id FOREIGN KEY (issue_id)
        REFERENCES ISSUE (issue_id) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - ISSUE_UPDATR_LOG(issue_id)
-- ALTER TABLE ISSUE_UPDATR_LOG
-- DROP FOREIGN KEY FK_ISSUE_UPDATR_LOG_issue_id_ISSUE_issue_id;

-- Foreign Key 설정 SQL - ISSUE_UPDATR_LOG(updated_by) -> MEMBER(id)
ALTER TABLE ISSUE_UPDATR_LOG
    ADD CONSTRAINT FK_ISSUE_UPDATR_LOG_updated_by_MEMBER_id FOREIGN KEY (updated_by)
        REFERENCES MEMBER (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - ISSUE_UPDATR_LOG(updated_by)
-- ALTER TABLE ISSUE_UPDATR_LOG
-- DROP FOREIGN KEY FK_ISSUE_UPDATR_LOG_updated_by_MEMBER_id;


-- ISSUE_ATTACHMENTS Table Create SQL
-- 테이블 생성 SQL - ISSUE_ATTACHMENTS
CREATE TABLE ISSUE_ATTACHMENTS
(
    `attachments_id`  BIGINT          NOT NULL    AUTO_INCREMENT COMMENT '첨부 파일 ID',
    `issue_id`        BIGINT          NULL        COMMENT '이슈 ID',
    `file_name`       VARCHAR(255)    NULL        COMMENT '파일 이름',
    `file_path`       TEXT            NULL        COMMENT '파일 경로',
    `upload_date`     DATE            NULL        COMMENT '업로드 일자',
    PRIMARY KEY (attachments_id)
);

-- Foreign Key 설정 SQL - ISSUE_ATTACHMENTS(issue_id) -> ISSUE(issue_id)
ALTER TABLE ISSUE_ATTACHMENTS
    ADD CONSTRAINT FK_ISSUE_ATTACHMENTS_issue_id_ISSUE_issue_id FOREIGN KEY (issue_id)
        REFERENCES ISSUE (issue_id) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - ISSUE_ATTACHMENTS(issue_id)
-- ALTER TABLE ISSUE_ATTACHMENTS
-- DROP FOREIGN KEY FK_ISSUE_ATTACHMENTS_issue_id_ISSUE_issue_id;


-- PROJECT_MEMBER Table Create SQL
-- 테이블 생성 SQL - PROJECT_MEMBER
CREATE TABLE PROJECT_MEMBER
(
    `member_id`   VARCHAR(255)    NULL        COMMENT '멤버_아이디',
    `project_id`  BIGINT          NULL        COMMENT '프로젝트_아이디'
);

-- 테이블 Comment 설정 SQL - PROJECT_MEMBER
ALTER TABLE PROJECT_MEMBER COMMENT '프로젝트 유저';

-- Foreign Key 설정 SQL - PROJECT_MEMBER(member_id) -> MEMBER(id)
ALTER TABLE PROJECT_MEMBER
    ADD CONSTRAINT FK_PROJECT_MEMBER_member_id_MEMBER_id FOREIGN KEY (member_id)
        REFERENCES MEMBER (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - PROJECT_MEMBER(member_id)
-- ALTER TABLE PROJECT_MEMBER
-- DROP FOREIGN KEY FK_PROJECT_MEMBER_member_id_MEMBER_id;

-- Foreign Key 설정 SQL - PROJECT_MEMBER(project_id) -> PROJECT(project_id)
ALTER TABLE PROJECT_MEMBER
    ADD CONSTRAINT FK_PROJECT_MEMBER_project_id_PROJECT_project_id FOREIGN KEY (project_id)
        REFERENCES PROJECT (project_id) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - PROJECT_MEMBER(project_id)
-- ALTER TABLE PROJECT_MEMBER
-- DROP FOREIGN KEY FK_PROJECT_MEMBER_project_id_PROJECT_project_id;


