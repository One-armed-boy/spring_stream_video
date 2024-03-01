-- Member - Video 간 참조를 위한 칼럼 추가
ALTER TABLE videos ADD COLUMN member_id BIGINT;
