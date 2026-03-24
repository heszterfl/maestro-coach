package com.maestrocoach.api;

import com.maestrocoach.api.dto.CreateTeacherRequest;
import com.maestrocoach.api.dto.PagedResponse;
import com.maestrocoach.api.dto.StudentResponse;
import com.maestrocoach.api.dto.TeacherResponse;
import com.maestrocoach.domain.Student;
import com.maestrocoach.domain.Teacher;
import com.maestrocoach.service.StudentService;
import com.maestrocoach.service.TeacherService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public TeacherResponse createTeacher(@RequestBody @Valid CreateTeacherRequest request) {
        Teacher teacher = teacherService.createTeacher(request.fullName(), request.email());
        return new TeacherResponse(teacher.getId(), teacher.getFullName(), teacher.getEmail());
    }

    @GetMapping
    public List<TeacherResponse> getAllTeachers() {

        return teacherService.getAllTeachers().stream()
                .map(t -> new TeacherResponse(
                        t.getId(),
                        t.getFullName(),
                        t.getEmail()
                ))
                .toList();
    }

    @GetMapping("/{teacherId}")
    public TeacherResponse getTeacherById(@PathVariable UUID teacherId) {
        Teacher teacher = teacherService.getTeacherById(teacherId);
        return new TeacherResponse(teacher.getId(), teacher.getFullName(), teacher.getEmail());
    }

    @GetMapping("/{teacherId}/students")
    public PagedResponse<StudentResponse> getStudentsByTeacher(
            @PathVariable UUID teacherId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Page<Student> studentPage = studentService.getStudentsByTeacher(teacherId, page, size);

        List<StudentResponse> studentList = studentPage.getContent().stream()
                .map(s -> new StudentResponse(
                        s.getId(),
                        s.getFullName(),
                        s.getEmail(),
                        s.getInstrument(),
                        s.getTeacher() != null ? s.getTeacher().getId() : null
                ))
                .toList();

        return new PagedResponse<>(
                studentList,
                studentPage.getNumber(),
                studentPage.getSize(),
                studentPage.getTotalElements(),
                studentPage.getTotalPages()
        );
    }

}
