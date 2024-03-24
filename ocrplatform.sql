-- DROP DATABASE IF EXISTS ocrplatform;
-- CREATE DATABASE ocrplatform;
-- USE ocrplatform;

CREATE TABLE `tb_paper` (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT,
                            `subject_id` bigint(20) NOT NULL,
                            `past_year` year(4) DEFAULT NULL,
                            `past_month` int(11) DEFAULT NULL,
                            `grade` int(11) DEFAULT NULL,
                            `exam_type` int(11) NOT NULL COMMENT '1:모의평가,2: 학력평가, 3:수능',
                            `exam_name` varchar(50) DEFAULT NULL,
                            `creator` bigint(20) NOT NULL,
                            `create_date` timestamp NULL DEFAULT current_timestamp(),
                            `worker` bigint(20) DEFAULT NULL,
                            `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '0: 작업 대기 1: 작업 시작',
                            `item_cnt` int(11) NOT NULL COMMENT '사용자입력 총 문항수',
                            `updater` bigint(20) DEFAULT NULL,
                            `update_date` timestamp NULL DEFAULT current_timestamp(),
                            PRIMARY KEY (`id`)
);

CREATE TABLE `tb_member` (
                             `id` bigint(20) NOT NULL AUTO_INCREMENT,
                             `department` int(11) NOT NULL COMMENT '1:디지털사업부, 2부터 부서 설정',
                             `name` varchar(50) NOT NULL,
                             `login_id` varchar(50) NOT NULL,
                             `password` varchar(500) DEFAULT NULL,
                             `start_date` timestamp NOT NULL COMMENT '사용기간 시작일',
                             `end_date` timestamp NOT NULL COMMENT '사용기간 종료일',
                             `status` int(11) NOT NULL DEFAULT 0 COMMENT '0: 계정 활성화, 1: 비활성화(로그인 불가)',
                             `role` int(11) NOT NULL COMMENT '1: 총괄관리자,  2 :수집담당자, 3: 문제운영자, 4: 문제검수자',
                             `create_date` timestamp NOT NULL DEFAULT current_timestamp() COMMENT '계정 등록일',
                             `creator` bigint(20) NOT NULL,
                             `update_date` timestamp NULL DEFAULT NULL COMMENT '계정 수정일',
                             `updater` bigint(20) DEFAULT NULL,
                             `delete_date` timestamp NULL DEFAULT NULL COMMENT '계정 삭제일',
                             `deleter` bigint(20) DEFAULT NULL,
                             `login_flag` int(11) DEFAULT 0,
                             PRIMARY KEY (`id`)
);

CREATE TABLE `tb_notice` (
                             `id` bigint(20) NOT NULL AUTO_INCREMENT,
                             `creator` bigint(20) NOT NULL,
                             `title` varchar(50) NOT NULL,
                             `content` varchar(500) NOT NULL,
                             `create_date` timestamp NOT NULL DEFAULT current_timestamp(),
                             `update_date` timestamp NULL DEFAULT NULL,
                             `updater` bigint(20) DEFAULT NULL,
                             `delete_date` timestamp NULL DEFAULT NULL,
                             `deleter` bigint(20) DEFAULT NULL,
                             PRIMARY KEY (`id`)
);

CREATE TABLE `tb_item` (
                           `id` bigint(20) NOT NULL AUTO_INCREMENT,
                           `paper_id` bigint(20) NOT NULL,
                           `ocr_result` text NOT NULL DEFAULT 0,
                           `creator` bigint(20) NOT NULL,
                           `create_date` timestamp NOT NULL DEFAULT current_timestamp(),
                           `updater` bigint(20) DEFAULT NULL,
                           `update_date` timestamp NULL DEFAULT NULL,
                           `ocr_progress` int(11) NOT NULL DEFAULT 0,
                           `item_order` int(11) NOT NULL,
                           PRIMARY KEY (`id`)
);

CREATE TABLE `tb_paper_upload` (
                                   `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                   `paper_id` bigint(20) NOT NULL,
                                   `path` varchar(500) NOT NULL,
                                   `original_file` varchar(500) NOT NULL,
                                   `upload_file` varchar(500) NOT NULL,
                                   `order` int(11) DEFAULT NULL,
                                   `creator` bigint(20) NOT NULL,
                                   `create_date` timestamp NOT NULL DEFAULT current_timestamp(),
                                   `updater` bigint(20) DEFAULT NULL,
                                   `update_date` timestamp NULL DEFAULT current_timestamp(),
                                   PRIMARY KEY (`id`)
);

CREATE TABLE `tb_item_image` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                 `item_id` bigint(20) NOT NULL,
                                 `path` varchar(500) NOT NULL,
                                 `original_file` varchar(500) NOT NULL,
                                 `upload_file` varchar(500) NOT NULL,
                                 `creator` bigint(20) NOT NULL,
                                 `create_date` timestamp NOT NULL DEFAULT current_timestamp(),
                                 `updater` bigint(20) DEFAULT NULL,
                                 `update_date` timestamp NULL DEFAULT NULL,
                                 `image_type` varchar(10) DEFAULT NULL COMMENT '이미지: P, 텍스트: T',
                                 PRIMARY KEY (`id`)
);

CREATE TABLE `tb_operation_log` (
                                    `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                    `member_id` bigint(20) NOT NULL,
                                    `log_timestamp` timestamp NOT NULL DEFAULT current_timestamp(),
                                    `item_id` bigint(20) NOT NULL,
                                    PRIMARY KEY (`id`)
);

CREATE TABLE `tb_login_log` (
                                `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                `member_id` bigint(20) NOT NULL,
                                `log_timestamp` timestamp NOT NULL DEFAULT current_timestamp(),
                                `login_flag` tinyint(4) NOT NULL DEFAULT 1 COMMENT '1: 성공, 0 : 실패',
                                PRIMARY KEY (`id`)
);


CREATE TABLE `tb_subject` (
                              `id` bigint(20) NOT NULL AUTO_INCREMENT,
                              `parent_id` bigint(20) DEFAULT NULL,
                              `depth` int(11) NOT NULL DEFAULT 1,
                              `name` varchar(30) NOT NULL,
                              `order` int(11) NOT NULL DEFAULT 1,
                              `creator` bigint(20) DEFAULT NULL,
                              `create_date` timestamp NULL DEFAULT current_timestamp(),
                              `updater` bigint(20) DEFAULT NULL,
                              `update_date` timestamp NULL DEFAULT NULL,
                              PRIMARY KEY (`id`)
);

INSERT INTO `tb_subject` (`name`, `depth`, `order`) VALUES ('국어', 1, 1);
INSERT INTO `tb_subject` (`name`, `depth`, `order`) VALUES ('수학', 1, 2);
INSERT INTO `tb_subject` (`name`, `depth`, `order`) VALUES ('영어', 1, 3);
INSERT INTO `tb_subject` (`name`, `depth`, `order`) VALUES ('한국사', 1, 4);
INSERT INTO `tb_subject` (`name`, `depth`, `order`) VALUES ('사회탐구', 1, 5);
INSERT INTO `tb_subject` (`name`, `depth`, `order`) VALUES ('과학탐구', 1, 6);
INSERT INTO `tb_subject` (`name`, `depth`, `order`) VALUES ('직업탐구', 1, 7);
INSERT INTO `tb_subject` (`name`, `depth`, `order`) VALUES ('제2외국어/한문', 1, 8);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (1, '화법과 작문', 2, 1);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (1, '언어와 매체', 2, 2);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (2, '확률과 통계', 2, 1);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (2, '미적분', 2, 2);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (2, '기하', 2, 3);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (3, '영어', 2, 1);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (4, '한국사', 2, 1);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (5, '생활과 윤리', 2, 1);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (5, '윤리와 사상', 2, 2);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (5, '한국지리', 2, 3);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (5, '세계지리', 2, 4);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (5, '동아시아사', 2, 5);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (5, '세계사', 2, 6);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (5, '경제', 2, 7);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (5, '정치와 법', 2, 8);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (5, '사회 문화', 2, 9);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (6, '물리학I', 2, 1);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (6, '화학I', 2, 2);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (6, '생명과학I', 2, 3);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (6, '지구과학I', 2, 4);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (6, '물리학II', 2, 5);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (6, '화학II', 2, 6);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (6, '생명과학II', 2, 7);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (6, '지구과학II', 2, 8);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (7, '농업 기초 기술', 2, 1);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (7, '공업 일반', 2, 2);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (7, '상업 경제', 2, 3);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (7, '수산 해운 산업 기초', 2, 4);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (7, '인간 발달', 2, 5);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (7, '성공적인 직업생활', 2, 6);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (8, '독일어I', 2, 1);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (8, '프랑스어I', 2, 2);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (8, '스페인어I', 2, 3);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (8, '중국어I', 2, 4);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (8, '일본어I', 2, 5);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (8, '러시아어I', 2, 6);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (8, '아랍어I', 2, 7);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (8, '베트남어I', 2, 8);
INSERT INTO `tb_subject` (`parent_id`, `name`, `depth`, `order`) VALUES (8, '한문I', 2, 9);

ALTER TABLE `tb_paper` ADD CONSTRAINT `FK_tb_subject_TO_tb_paper_1` FOREIGN KEY (
	`subject_id`
)
REFERENCES `tb_subject` (
	`id`
);

ALTER TABLE `tb_paper` ADD CONSTRAINT `FK_tb_member_TO_tb_paper_1` FOREIGN KEY (
	`creator`
)
REFERENCES `tb_member` (
	`id`
);

ALTER TABLE `tb_notice` ADD CONSTRAINT `FK_tb_member_TO_tb_notice_1` FOREIGN KEY (
	`creator`
)
REFERENCES `tb_member` (
	`id`
);

ALTER TABLE `tb_item` ADD CONSTRAINT `FK_tb_paper_TO_tb_item_1` FOREIGN KEY (
	`paper_id`
)
REFERENCES `tb_paper` (
	`id`
);

ALTER TABLE `tb_paper_upload` ADD CONSTRAINT `FK_tb_paper_TO_tb_paper_upload_1` FOREIGN KEY (
	`paper_id`
)
REFERENCES `tb_paper` (
	`id`
);

ALTER TABLE `tb_item_image` ADD CONSTRAINT `FK_tb_item_TO_tb_item_image_1` FOREIGN KEY (
	`item_id`
)
REFERENCES `tb_item` (
	`id`
);

ALTER TABLE `tb_operation_log` ADD CONSTRAINT `FK_tb_member_TO_tb_operation_log_1` FOREIGN KEY (
	`member_id`
)
REFERENCES `tb_member` (
	`id`
);

ALTER TABLE `tb_operation_log` ADD CONSTRAINT `FK_tb_item_TO_tb_operation_log_1` FOREIGN KEY (
	`item_id`
)
REFERENCES `tb_item` (
	`id`
);

ALTER TABLE `tb_login_log` ADD CONSTRAINT `FK_tb_member_TO_tb_login_log_1` FOREIGN KEY (
	`member_id`
)
REFERENCES `tb_member` (
	`id`
);

ALTER TABLE `tb_subject` ADD CONSTRAINT `FK_tb_subject_TO_tb_subject_1` FOREIGN KEY (
	`parent_id`
)
REFERENCES `tb_subject` (
	`id`
);
