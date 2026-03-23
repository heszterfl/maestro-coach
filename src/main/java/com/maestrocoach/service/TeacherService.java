package com.maestrocoach.service;

import com.maestrocoach.api.error.ResourceNotFoundException;
import com.maestrocoach.domain.Teacher;
import com.maestrocoach.persistence.InMemoryTeacherStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TeacherService {

    private final InMemoryTeacherStore teacherStore;

    public TeacherService(InMemoryTeacherStore teacherStore) {
        this.teacherStore = teacherStore;
    }

    public Teacher createTeacher(String fullName, String email) {
        Teacher newTeacher = new Teacher(fullName, email);
        return teacherStore.save(newTeacher);
    }

    public Teacher getTeacherById(UUID teacherId) {
        return teacherStore.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + teacherId));
    }

    public List<Teacher> getAllTeachers() {
        return teacherStore.findAll();
    }
}
