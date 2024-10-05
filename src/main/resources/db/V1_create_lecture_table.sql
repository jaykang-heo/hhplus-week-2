CREATE TABLE lectures (
      id BIGSERIAL PRIMARY KEY,
      title VARCHAR(255) NOT NULL,
      teacher_id BIGINT NOT NULL,
      date_utc TIMESTAMP NOT NULL,
      registered_count INTEGER NOT NULL DEFAULT 0
);

CREATE INDEX idx_lectures_title ON lectures(title);
CREATE INDEX idx_lectures_date_utc ON lectures(date_utc);