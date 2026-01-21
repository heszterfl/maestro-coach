package com.maestrocoach.service;

import com.maestrocoach.domain.Teacher;
import com.maestrocoach.persistence.InMemoryTeacherStore;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TeacherService {

    private final InMemoryTeacherStore teacherStore;

    public TeacherService(InMemoryTeacherStore teacherStore) {
        this.teacherStore = teacherStore;
    }

    public Teacher createTeacher(String fullName, String email) {
        Teacher newTeacher = new Teacher(fullName, email);
        return teacherStore.save(newTeacher);
    }

    public Optional<Teacher> getTeacherById(UUID teacherId) {
        return teacherStore.findById(teacherId);
    }

    public List<Teacher> getAllTeachers() {
        return teacherStore.findAll();
    }
}
