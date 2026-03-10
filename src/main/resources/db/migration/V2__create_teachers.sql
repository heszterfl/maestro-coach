CREATE TABLE teachers (
    id UUID PRIMARY KEY,
    full_name TEXT NOT NULL,
    email TEXT NOT NULL
);

ALTER TABLE students
ADD COLUMN teacher_id UUID;

ALTER TABLE students
ADD CONSTRAINT fk_teacher
FOREIGN KEY (teacher_id)
REFERENCES teachers(id);