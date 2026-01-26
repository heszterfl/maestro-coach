package com.maestrocoach.api;

import com.maestrocoach.api.dto.CreateStudentRequest;
import com.maestrocoach.api.dto.StudentResponse;
import com.maestrocoach.domain.Student;
import com.maestrocoach.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponse createStudent(@RequestBody @Valid CreateStudentRequest request) {
        Student student = studentService.createStudent(request.fullName(), request.email(), request.instrument());
        return new StudentResponse(student.getId(), student.getFullName(), student.getEmail(), student.getInstrument(), student.getTeacherId());
    }

    @PostMapping("/{studentId}/assign-teacher/{teacherId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void assignTeacher(@PathVariable UUID studentId, @PathVariable UUID teacherId) {
        studentService.assignStudentToTeacher(studentId, teacherId);
    }
}
