package com.maestrocoach.service;

import com.maestrocoach.domain.Student;
import com.maestrocoach.domain.Teacher;
import com.maestrocoach.repository.StudentRepository;
import com.maestrocoach.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
        Student student = studentRepository.findById(studentId)
                .orElseThrow(IllegalArgumentException::new);

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(IllegalArgumentException::new);

        student.assignTeacher(teacher);
        studentRepository.save(student);
    }

    public List<Student> getStudentsByTeacher(UUID teacherId) {
        return studentRepository.findByTeacher_Id(teacherId);
    }

    public Optional<Student> getStudentById(UUID studentId) {
        return studentRepository.findById(studentId);
    }
}
