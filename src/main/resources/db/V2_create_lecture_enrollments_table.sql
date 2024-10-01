CREATE TABLE lecture_enrollments (
     id BIGSERIAL PRIMARY KEY,
     user_id BIGINT NOT NULL,
     lecture_id BIGINT NOT NULL,
     enrolled_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_lecture_enrollments_user_id ON lecture_enrollments(user_id);
CREATE INDEX idx_lecture_enrollments_lecture_id ON lecture_enrollments(lecture_id);
CREATE UNIQUE INDEX uq_lecture_enrollments_user_lecture ON lecture_enrollments(user_id, lecture_id);