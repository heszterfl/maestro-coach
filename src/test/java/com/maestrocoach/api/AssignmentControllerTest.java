package com.maestrocoach.api;

import com.maestrocoach.api.error.ResourceNotFoundException;
import com.maestrocoach.domain.Assignment;
import com.maestrocoach.domain.LearningItem;
import com.maestrocoach.domain.Student;
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

import static com.maestrocoach.domain.LearningCategory.INSTRUMENT_PRACTICE;
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
        Student student = new Student("Anna Bellman", "anna@bellman.com", "piano");
        LearningItem learningItem = new LearningItem("C-Dúr Etűd", INSTRUMENT_PRACTICE, null);
        Assignment a = new Assignment(student, learningItem);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("studentId", student.getId().toString());
        jsonObject.put("learningItemId", learningItem.getId().toString());

        Mockito.when(assignmentService.createAssignment(student.getId(), learningItem.getId()))
                .thenReturn(a);

        mockMvc.perform(post("/api/assignments").contentType(MediaType.APPLICATION_JSON).content(jsonObject.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(a.getId().toString()))
                .andExpect(jsonPath("$.studentId").value(a.getStudent().getId().toString()))
                .andExpect(jsonPath("$.learningItemId").value(a.getLearningItem().getId().toString()))
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
        Student student = new Student("Anna Bellman", "anna@bellman.com", "piano");
        LearningItem learningItem = new LearningItem("C-Dúr Etűd", INSTRUMENT_PRACTICE, null);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("studentId", student.getId().toString());
        jsonObject.put("learningItemId", learningItem.getId().toString());

        Mockito.doThrow(new ResourceNotFoundException("Student not found with id: " + student.getId())).when(assignmentService).createAssignment(student.getId(), learningItem.getId());

        mockMvc.perform(post("/api/assignments").contentType(MediaType.APPLICATION_JSON).content(jsonObject.toString()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Student not found with id: " + student.getId()));
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

        Mockito.doThrow(new ResourceNotFoundException("Assignment not found with id: " + randomId)).when(assignmentService).completeAssignment(randomId);

        mockMvc.perform(post("/api/assignments/{assignmentId}/complete", randomId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Assignment not found with id: " + randomId));
    }
}
