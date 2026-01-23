package com.maestrocoach.api;

import com.maestrocoach.domain.Student;
import com.maestrocoach.domain.Teacher;
import com.maestrocoach.service.StudentService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService service;

    @Test
    void createStudent_returns201AndBody() throws Exception {
        Student s = new Student("Anna Bellman", "anna@bellman.com", "piano");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fullName", s.getFullName());
        jsonObject.put("email", s.getEmail());
        jsonObject.put("instrument", s.getInstrument());

        Mockito.when(service.createStudent(s.getFullName(), s.getEmail(), s.getInstrument()))
                .thenReturn(s);

        mockMvc.perform(post("/api/students").contentType(MediaType.APPLICATION_JSON).content(jsonObject.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(s.getId().toString()))
                .andExpect(jsonPath("$.fullName").value("Anna Bellman"))
                .andExpect(jsonPath("$.email").value("anna@bellman.com"))
                .andExpect(jsonPath("$.instrument").value("piano"))
                .andExpect(jsonPath("$.teacherId").value(nullValue()));
    }

    @Test
    void assignTeacher_returns204() throws Exception {
        Teacher t = new Teacher("John Doe", "john@school.com");
        Student s = new Student("Anna Bellman", "anna@bellman.com", "piano");

        Mockito.doNothing().when(service).assignStudentToTeacher(s.getId(), t.getId());

        mockMvc.perform(post("/api/students/{studentId}/assign-teacher/{teacherId}", s.getId(), t.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void assignTeacher_invalidIds_returns404() throws Exception {
        UUID randomId = UUID.randomUUID();
        Student s = new Student("Anna Bellman", "anna@bellman.com", "piano");

        Mockito.doThrow(IllegalArgumentException.class).when(service).assignStudentToTeacher(s.getId(), randomId);

        mockMvc.perform(post("/api/students/{studentId}/assign-teacher/{teacherId}", s.getId(), randomId))
                .andExpect(status().isNotFound());
    }
}
