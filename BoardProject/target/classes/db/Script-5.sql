
-- users -> board -> file 순으로 생성

-- public.users definition

-- Drop table

-- DROP TABLE public.users;

CREATE TABLE public.users (
	user_id varchar(36) NOT NULL, -- 사용자 id
	"name" text NOT NULL, -- 이름
	CONSTRAINT users_pkey PRIMARY KEY (user_id)
);

-- Column comments

COMMENT ON COLUMN public.users.user_id IS '사용자 id';
COMMENT ON COLUMN public.users."name" IS '이름';

-- public.board definition

-- Drop table

-- DROP TABLE public.board;

CREATE TABLE public.board (
	board_id varchar(36) NOT NULL, -- 게시글 id
	title text NOT NULL, -- 제목
	user_id varchar(36) NULL, -- 작성자 id
	create_date timestamp NULL DEFAULT now(), -- 등록일시
	click_count int4 NOT NULL DEFAULT 0, -- 조회수
	"content" text NOT NULL, -- 글 내용
	"password" varchar(64) NOT NULL, -- 글 비밀번호
	is_answer bool NULL, -- 답글 여부
	root_id varchar(36) NULL, -- 최상위글 id
	exist_file bool NULL, -- 파일 여부
	parent_id varchar(36) NULL,
	CONSTRAINT board_pkey PRIMARY KEY (board_id)
);

-- Column comments

COMMENT ON COLUMN public.board.board_id IS '게시글 id';
COMMENT ON COLUMN public.board.title IS '제목';
COMMENT ON COLUMN public.board.user_id IS '작성자 id';
COMMENT ON COLUMN public.board.create_date IS '등록일시';
COMMENT ON COLUMN public.board.click_count IS '조회수';
COMMENT ON COLUMN public.board."content" IS '글 내용';
COMMENT ON COLUMN public.board."password" IS '글 비밀번호';
COMMENT ON COLUMN public.board.is_answer IS '답글 여부';
COMMENT ON COLUMN public.board.root_id IS '최상위글 id';
COMMENT ON COLUMN public.board.exist_file IS '파일 여부';


-- public.board foreign keys

ALTER TABLE public.board ADD CONSTRAINT fk_board_parent FOREIGN KEY (parent_id) REFERENCES public.board(board_id);
ALTER TABLE public.board ADD CONSTRAINT fk_board_root_id FOREIGN KEY (root_id) REFERENCES public.board(board_id);
ALTER TABLE public.board ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES public.users(user_id);

-- public.file definition

-- Drop table

-- DROP TABLE public.file;

CREATE TABLE public.file (
	file_id varchar(36) NOT NULL, -- 파일 id
	board_id varchar(36) NULL, -- 게시글 id
	original_name text NOT NULL, -- 업로드 파일명
	file_path text NOT NULL, -- 저장된 폴더명
	save_name varchar(45) NOT NULL, -- 저장된 파일명
	file_size int8 NOT NULL DEFAULT 0, -- 파일 크기
	CONSTRAINT file_pkey PRIMARY KEY (file_id)
);

-- Column comments

COMMENT ON COLUMN public.file.file_id IS '파일 id';
COMMENT ON COLUMN public.file.board_id IS '게시글 id';
COMMENT ON COLUMN public.file.original_name IS '업로드 파일명';
COMMENT ON COLUMN public.file.file_path IS '저장된 폴더명';
COMMENT ON COLUMN public.file.save_name IS '저장된 파일명';
COMMENT ON COLUMN public.file.file_size IS '파일 크기';


-- public.file foreign keys

ALTER TABLE public.file ADD CONSTRAINT fk_board_id FOREIGN KEY (board_id) REFERENCES public.board(board_id);