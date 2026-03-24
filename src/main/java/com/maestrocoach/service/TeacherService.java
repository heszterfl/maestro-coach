package com.maestrocoach.service;

import com.maestrocoach.api.error.ResourceNotFoundException;
import com.maestrocoach.domain.Teacher;
import com.maestrocoach.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public Teacher createTeacher(String fullName, String email) {
        Teacher newTeacher = new Teacher(fullName, email);
        return teacherRepository.save(newTeacher);
    }

    public Teacher getTeacherById(UUID teacherId) {
        return teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + teacherId));
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }
}
