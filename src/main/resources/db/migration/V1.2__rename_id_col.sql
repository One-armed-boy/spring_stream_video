-- 테이블 ID 참조에 대한 명확성을 위해 칼럼 명 수정
ALTER TABLE members RENAME COLUMN id to member_id;
ALTER TABLE roles RENAME COLUMN id to role_id;
ALTER TABLE videos RENAME COLUMN id to video_id;
