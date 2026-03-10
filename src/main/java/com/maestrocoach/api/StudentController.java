package com.maestrocoach.api;

import com.maestrocoach.api.dto.AssignmentResponse;
import com.maestrocoach.api.dto.CreateStudentRequest;
import com.maestrocoach.api.dto.StudentResponse;
import com.maestrocoach.domain.Assignment;
import com.maestrocoach.domain.AssignmentStatus;
import com.maestrocoach.domain.Student;
import com.maestrocoach.service.AssignmentService;
import com.maestrocoach.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;
    private final AssignmentService assignmentService;

    public StudentController(StudentService studentService, AssignmentService assignmentService) {
        this.studentService = studentService;
        this.assignmentService = assignmentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponse createStudent(@RequestBody @Valid CreateStudentRequest request) {
        Student student = studentService.createStudent(request.fullName(), request.email(), request.instrument());
        return new StudentResponse(student.getId(), student.getFullName(), student.getEmail(), student.getInstrument(), student.getTeacher() != null ? student.getTeacher().getId() : null);
    }

    @PostMapping("/{studentId}/assign-teacher/{teacherId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void assignTeacher(@PathVariable UUID studentId, @PathVariable UUID teacherId) {
        studentService.assignStudentToTeacher(studentId, teacherId);
    }

    @GetMapping("/{studentId}/assignments")
    public List<AssignmentResponse> listAssignmentsByStudent(@PathVariable UUID studentId, @RequestParam(required = false) AssignmentStatus status) {

        if (studentService.getStudentById(studentId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
        }

        List<Assignment> assignmentList;
        if (status == null) {
            assignmentList = assignmentService.getAssignmentsByStudent(studentId);
        } else {
            assignmentList = assignmentService.getAssignmentsByStudent(studentId, status);
        }

        List<AssignmentResponse> responseList = new ArrayList<>();
        for (Assignment assignment : assignmentList) {
            AssignmentResponse response = new AssignmentResponse(assignment.getId(), assignment.getStudentId(), assignment.getLearningItemId(), assignment.getStatus());
            responseList.add(response);
        }
        return responseList;
    }
}
