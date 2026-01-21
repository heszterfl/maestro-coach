package com.maestrocoach.domain;

import java.util.UUID;

public class Student {

    private UUID id;
    private String fullName;
    private String email;
    private String instrument;
    private UUID teacherId;

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

    public UUID getTeacherId() {
        return teacherId;
    }

    public void assignTeacher(UUID teacherId) {
        this.teacherId = teacherId;
    }
}
