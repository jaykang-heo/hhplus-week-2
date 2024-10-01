CREATE TABLE teachers (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

CREATE INDEX idx_teachers_name ON teachers(name);