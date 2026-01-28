package com.maestrocoach.api;

import com.maestrocoach.domain.Assignment;
import com.maestrocoach.domain.Student;
import com.maestrocoach.domain.Teacher;
import com.maestrocoach.service.AssignmentService;
import com.maestrocoach.service.StudentService;
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
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService service;

    @MockitoBean
    private AssignmentService assignmentService;

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
    void createStudent_emptyInstrumentParameter_returns400() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fullName", "Anna Bellman");
        jsonObject.put("email", "anna@bellman.com");
        jsonObject.put("instrument", "");

        mockMvc.perform(post("/api/students").contentType(MediaType.APPLICATION_JSON).content(jsonObject.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/students"));
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

    @Test
    void getAssignmentsByStudent_returns200AndList() throws Exception {
        Student s = new Student("Anna Bellman", "anna@bellman.com", "piano");
        UUID itemId1 = UUID.randomUUID();
        UUID itemId2 = UUID.randomUUID();
        Assignment a1 = new Assignment(s.getId(), itemId1);
        Assignment a2 = new Assignment(s.getId(), itemId2);

        Mockito.when(service.getStudentById(s.getId()))
                .thenReturn(Optional.of(s));
        Mockito.when(assignmentService.getAssignmentsByStudent(s.getId()))
                .thenReturn(List.of(a1, a2));

        mockMvc.perform(get("/api/students/{studentId}/assignments", s.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(a1.getId().toString()))
                .andExpect(jsonPath("$[0].studentId").value(a1.getStudentId().toString()))
                .andExpect(jsonPath("$[0].learningItemId").value(a1.getLearningItemId().toString()))
                .andExpect(jsonPath("$[0].status").value("ASSIGNED"))
                .andExpect(jsonPath("$[1].id").value(a2.getId().toString()))
                .andExpect(jsonPath("$[1].studentId").value(a2.getStudentId().toString()))
                .andExpect(jsonPath("$[1].learningItemId").value(a2.getLearningItemId().toString()))
                .andExpect(jsonPath("$[1].status").value("ASSIGNED"));

    }

    @Test
    void getAssignmentsByStudent_studentNotFound_returns404() throws Exception {
        UUID studentId = UUID.randomUUID();

        Mockito.when(service.getStudentById(studentId))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/students/{studentId}/assignments", studentId))
                .andExpect(status().isNotFound());
    }
}
