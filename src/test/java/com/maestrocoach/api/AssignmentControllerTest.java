package com.maestrocoach.api;

import com.maestrocoach.domain.Assignment;
import com.maestrocoach.service.AssignmentService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AssignmentController.class)
public class AssignmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    AssignmentService assignmentService;

    @Test
    void createAssignment_returns201AndBody() throws Exception {
        UUID studentId = UUID.randomUUID();
        UUID learningItemId = UUID.randomUUID();
        Assignment a = new Assignment(studentId, learningItemId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("studentId", studentId.toString());
        jsonObject.put("learningItemId", learningItemId.toString());

        Mockito.when(assignmentService.createAssignment(a.getStudentId(), a.getLearningItemId()))
                .thenReturn(a);

        mockMvc.perform(post("/api/assignments").contentType(MediaType.APPLICATION_JSON).content(jsonObject.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(a.getId().toString()))
                .andExpect(jsonPath("$.studentId").value(a.getStudentId().toString()))
                .andExpect(jsonPath("$.learningItemId").value(a.getLearningItemId().toString()))
                .andExpect(jsonPath("$.status").value("ASSIGNED"));
    }

    @Test
    void createAssignment_invalidBody_returns400() throws Exception {
        UUID learningItemId = UUID.randomUUID();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("studentId", null);
        jsonObject.put("learningItemId", learningItemId.toString());

        mockMvc.perform(post("/api/assignments").contentType(MediaType.APPLICATION_JSON).content(jsonObject.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/assignments"));
    }

    @Test
    void createAssignment_studentNotFound_returns404() throws Exception {
        UUID studentId = UUID.randomUUID();
        UUID learningItemId = UUID.randomUUID();
        Assignment a = new Assignment(studentId, learningItemId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("studentId", studentId.toString());
        jsonObject.put("learningItemId", learningItemId.toString());

        Mockito.doThrow(IllegalArgumentException.class).when(assignmentService).createAssignment(studentId, learningItemId);

        mockMvc.perform(post("/api/assignments").contentType(MediaType.APPLICATION_JSON).content(jsonObject.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void completeAssignment_returns204() throws Exception {
        UUID randomId = UUID.randomUUID();

        Mockito.doNothing().when(assignmentService).completeAssignment(randomId);

        mockMvc.perform(post("/api/assignments/{assignmentId}/complete", randomId))
                .andExpect(status().isNoContent());
    }

    @Test
    void completeAssignment_notFound_returns404() throws Exception {
        UUID randomId = UUID.randomUUID();

        Mockito.doThrow(IllegalArgumentException.class).when(assignmentService).completeAssignment(randomId);

        mockMvc.perform(post("/api/assignments/{assignmentId}/complete", randomId))
                .andExpect(status().isNotFound());
    }
}
