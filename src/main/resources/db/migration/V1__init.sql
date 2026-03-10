CREATE TABLE students (
    id UUID PRIMARY KEY,
    full_name TEXT NOT NULL,
    email TEXT NOT NULL,
    instrument TEXT NOT NULL
);

CREATE TABLE assignments (
    id UUID PRIMARY KEY,
    student_id UUID NOT NULL,
    item_id UUID NOT NULL,
    status TEXT NOT NULL,

    CONSTRAINT fk_assignments_student
        FOREIGN KEY (student_id)
        REFERENCES students(id)
);