package com.maestrocoach.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "teachers")
public class Teacher {

    @Id
    private UUID id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String email;

    @Transient
    private List<Student> students;

    public Teacher() {
        this.students = new ArrayList<>();
    }

    public Teacher(UUID id, String fullName, String email) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
    }

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
