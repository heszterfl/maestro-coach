package com.maestrocoach.api;

import com.maestrocoach.api.error.ResourceNotFoundException;
import com.maestrocoach.domain.Student;
import com.maestrocoach.domain.Teacher;
import com.maestrocoach.service.StudentService;
import com.maestrocoach.service.TeacherService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TeacherController.class)
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TeacherService service;

    @MockitoBean
    private StudentService studentService;

    @Test
    void createTeacher_returns201AndBody() throws Exception {
        Teacher t = new Teacher("John Doe", "john@school.com");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fullName", t.getFullName());
        jsonObject.put("email", t.getEmail());

        Mockito.when(service.createTeacher(t.getFullName(), t.getEmail()))
                .thenReturn(t);

        mockMvc.perform(post("/api/teachers").contentType(MediaType.APPLICATION_JSON).content(jsonObject.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(t.getId().toString()))
                .andExpect(jsonPath("$.fullName").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@school.com"));
    }

    @Test
    void createTeacher_invalidEmail_returns400() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fullName", "John Doe");
        jsonObject.put("email", "not-an-email");

        mockMvc.perform(post("/api/teachers").contentType(MediaType.APPLICATION_JSON).content(jsonObject.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/teachers"));
    }

    @Test
    void allTeachersReturned() throws Exception {
        Teacher t1 = new Teacher("Charlie Chaplin", "charlie@school.com");
        Teacher t2 = new Teacher("David Kay", "david@school.com");

        Mockito.when(service.getAllTeachers())
                .thenReturn(List.of(t1, t2));

        mockMvc.perform(get("/api/teachers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(t1.getId().toString()))
                .andExpect(jsonPath("$[0].fullName").value("Charlie Chaplin"))
                .andExpect(jsonPath("$[0].email").value("charlie@school.com"))
                .andExpect(jsonPath("$[1].id").value(t2.getId().toString()))
                .andExpect(jsonPath("$[1].fullName").value("David Kay"))
                .andExpect(jsonPath("$[1].email").value("david@school.com"));
    }

    @Test
    void getTeacherById_teacherNotFound_returns404() throws Exception {
        UUID randomId = UUID.randomUUID();

        Mockito.when(service.getTeacherById(randomId))
                .thenThrow(new ResourceNotFoundException("Teacher not found with id: " + randomId));

        mockMvc.perform(get("/api/teachers/{teacherId}", randomId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Teacher not found with id: " + randomId));
    }

    @Test
    void getStudentsByTeacher_returns200AndList() throws Exception {
        Teacher t = new Teacher("John Doe", "john@school.com");
        Student s1 = new Student("Anna Bellman", "anna@bellman.com", "piano");
        Student s2 = new Student("Emma Schmidt", "emma@schmidt.com", "piano");

        s1.assignTeacher(t);
        s2.assignTeacher(t);

        Page<Student> studentPage = new PageImpl<>(List.of(s1, s2), PageRequest.of(0, 10), 2);

        Mockito.when(studentService.getStudentsByTeacher(t.getId(), 0, 10))
                .thenReturn(studentPage);

        mockMvc.perform(get("/api/teachers/{teacherId}/students", t.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id").value(s1.getId().toString()))
                .andExpect(jsonPath("$.content[0].fullName").value("Anna Bellman"))
                .andExpect(jsonPath("$.content[0].email").value("anna@bellman.com"))
                .andExpect(jsonPath("$.content[0].instrument").value("piano"))
                .andExpect(jsonPath("$.content[0].teacherId").value(t.getId().toString()))
                .andExpect(jsonPath("$.content[1].id").value(s2.getId().toString()))
                .andExpect(jsonPath("$.content[1].fullName").value("Emma Schmidt"))
                .andExpect(jsonPath("$.content[1].email").value("emma@schmidt.com"))
                .andExpect(jsonPath("$.content[1].instrument").value("piano"))
                .andExpect(jsonPath("$.content[1].teacherId").value(t.getId().toString()))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void getStudentsByTeacher_teacherExistsButNoStudents_returns200AndEmptyList() throws Exception {
        Teacher t = new Teacher("John Doe", "john@school.com");

        Page<Student> studentPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);

        Mockito.when(studentService.getStudentsByTeacher(t.getId(), 0, 10))
                .thenReturn(studentPage);

        mockMvc.perform(get("/api/teachers/{teacherId}/students", t.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.totalPages").value(0));
    }

    @Test
    void getStudentsByTeacher_teacherNotFound_returns404() throws Exception {
        UUID randomId = UUID.randomUUID();

        Mockito.when(studentService.getStudentsByTeacher(randomId, 0, 10))
                .thenThrow(new ResourceNotFoundException("Teacher not found with id: " + randomId));

        mockMvc.perform(get("/api/teachers/{teacherId}/students", randomId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Teacher not found with id: " + randomId));
    }
}
