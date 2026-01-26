package com.maestrocoach.api;

import com.maestrocoach.api.dto.CreateTeacherRequest;
import com.maestrocoach.api.dto.StudentResponse;
import com.maestrocoach.api.dto.TeacherResponse;
import com.maestrocoach.domain.Student;
import com.maestrocoach.domain.Teacher;
import com.maestrocoach.service.StudentService;
import com.maestrocoach.service.TeacherService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;
    private final StudentService studentService;

    public TeacherController(TeacherService teacherService, StudentService studentService) {
        this.teacherService = teacherService;
        this.studentService = studentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeacherResponse createTeacher(@RequestBody CreateTeacherRequest request) {
        Teacher teacher = teacherService.createTeacher(request.fullName(), request.email());
        return new TeacherResponse(teacher.getId(), teacher.getFullName(), teacher.getEmail());
    }

    @GetMapping
    public List<TeacherResponse> getAllTeachers() {
        List<TeacherResponse> responseList = new ArrayList<>();
        List<Teacher> teacherList = teacherService.getAllTeachers();
        for (Teacher teacher : teacherList) {
            TeacherResponse response = new TeacherResponse(teacher.getId(), teacher.getFullName(), teacher.getEmail());
            responseList.add(response);
        }
        return responseList;
    }

    @GetMapping("/{teacherId}")
    public TeacherResponse getTeacherById (@PathVariable UUID teacherId) {
        Teacher teacher = teacherService.getTeacherById(teacherId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found"));
        return new TeacherResponse(teacher.getId(), teacher.getFullName(), teacher.getEmail());
    }

    @GetMapping("/{teacherId}/students")
    public List<StudentResponse> getStudentsByTeacher(@PathVariable UUID teacherId) {

        if (teacherService.getTeacherById(teacherId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found");
        }

        List<Student> studentList = studentService.getStudentsByTeacher(teacherId);
        List<StudentResponse> responseList = new ArrayList<>();
        for (Student student : studentList) {
            StudentResponse response = new StudentResponse(student.getId(), student.getFullName(), student.getEmail(), student.getInstrument(), student.getTeacherId());
            responseList.add(response);
        }

        return responseList;
    }

}
