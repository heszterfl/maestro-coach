package com.maestrocoach.service;

import com.maestrocoach.domain.Student;
import com.maestrocoach.domain.Teacher;
import com.maestrocoach.persistence.InMemoryStudentStore;
import com.maestrocoach.persistence.InMemoryTeacherStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentService {

    private final InMemoryStudentStore studentStore;
    private final InMemoryTeacherStore teacherStore;

    public StudentService(InMemoryStudentStore studentStore, InMemoryTeacherStore teacherStore) {
        this.studentStore = studentStore;
        this.teacherStore = teacherStore;
    }

    public Student createStudent(String fullName, String email, String instrument) {
        Student newStudent = new Student(fullName, email, instrument);
        return studentStore.save(newStudent);
    }

    public void assignStudentToTeacher(UUID studentId, UUID teacherId) {
        Optional<Student> studentOptional = studentStore.findById(studentId);
        Optional<Teacher> teacherOptional = teacherStore.findById(teacherId);
        if (studentOptional.isEmpty() || teacherOptional.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Student student = studentOptional.get();
        student.assignTeacher(teacherId);
    }

    public List<Student> getStudentsByTeacher(UUID teacherId) {
        return studentStore.findByTeacherId(teacherId);
    }

    public Optional<Student> getStudentById(UUID studentId) {
        return studentStore.findById(studentId);
    }
}
