package com.maestrocoach.service;

import com.maestrocoach.api.error.ResourceNotFoundException;
import com.maestrocoach.domain.Student;
import com.maestrocoach.domain.Teacher;
import com.maestrocoach.repository.StudentRepository;
import com.maestrocoach.repository.TeacherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public StudentService(StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    public Student createStudent(String fullName, String email, String instrument) {
        Student newStudent = new Student(fullName, email, instrument);
        return studentRepository.save(newStudent);
    }

    public void assignStudentToTeacher(UUID studentId, UUID teacherId) {
        Student student = findStudentOrThrow(studentId);
        Teacher teacher = findTeacherOrThrow(teacherId);

        student.assignTeacher(teacher);
        studentRepository.save(student);
    }

    public Page<Student> getStudentsByTeacher(UUID teacherId, int page, int size) {
        findTeacherOrThrow(teacherId);

        Pageable pageable = PageRequest.of(page, size);

        return studentRepository.findByTeacher_Id(teacherId, pageable);
    }

    public Student getStudentById(UUID studentId) {
        return findStudentOrThrow(studentId);
    }

    private Student findStudentOrThrow(UUID studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
    }

    private Teacher findTeacherOrThrow(UUID teacherId) {
        return teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + teacherId));
    }
}
