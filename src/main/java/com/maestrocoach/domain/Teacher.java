package com.maestrocoach.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Teacher {

    private final UUID id;
    private String fullName;
    private String email;
    private final List<Student> students;

    public Teacher(String fullName, String email) {
        this.id = UUID.randomUUID();
        this.fullName = fullName;
        this.email = email;
        this.students = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Student> getStudents() {
        return Collections.unmodifiableList(students);
    }

    public void addStudent(Student student) {
        this.students.add(student);
    }
}
