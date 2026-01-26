package com.maestrocoach.api;

import com.maestrocoach.domain.Student;
import com.maestrocoach.domain.Teacher;
import com.maestrocoach.service.StudentService;
import com.maestrocoach.service.TeacherService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
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
    void teacherIdNotInStoreReturnsNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();

        Mockito.when(service.getTeacherById(randomId))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/teachers/{teacherId}", randomId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getStudentsByTeacher_success() throws Exception {
        Teacher t = new Teacher("John Doe", "john@school.com");
        Student s1 = new Student("Anna Bellman", "anna@bellman.com", "piano");
        Student s2 = new Student("Emma Schmidt", "emma@schmidt.com", "piano");

        Mockito.when(service.getTeacherById(t.getId()))
                .thenReturn(Optional.of(t));

        s1.assignTeacher(t.getId());
        s2.assignTeacher(t.getId());

        Mockito.when(studentService.getStudentsByTeacher(t.getId()))
                .thenReturn(List.of(s1, s2));

        mockMvc.perform(get("/api/teachers/{teacherId}/students", t.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(s1.getId().toString()))
                .andExpect(jsonPath("$[0].fullName").value("Anna Bellman"))
                .andExpect(jsonPath("$[0].email").value("anna@bellman.com"))
                .andExpect(jsonPath("$[0].instrument").value("piano"))
                .andExpect(jsonPath("$[0].teacherId").value(t.getId().toString()))
                .andExpect(jsonPath("$[1].id").value(s2.getId().toString()))
                .andExpect(jsonPath("$[1].fullName").value("Emma Schmidt"))
                .andExpect(jsonPath("$[1].email").value("emma@schmidt.com"))
                .andExpect(jsonPath("$[1].instrument").value("piano"))
                .andExpect(jsonPath("$[1].teacherId").value(t.getId().toString()));
    }

    @Test
    void getStudentsByTeacher_returnsNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();

        Mockito.when(service.getTeacherById(randomId))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/teachers/{teacherId}/students", randomId))
                .andExpect(status().isNotFound());
    }
}
