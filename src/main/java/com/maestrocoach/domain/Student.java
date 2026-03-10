package com.maestrocoach.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "students")
public class Student {

    @Id
    private UUID id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String instrument;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    public Student() {
    }

    public Student(UUID id, String fullName, String email, String instrument) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.instrument = instrument;
    }

    public Student(String fullName, String email, String instrument) {
        this.id = UUID.randomUUID();
        this.fullName = fullName;
        this.email = email;
        this.instrument = instrument;
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

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void assignTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
