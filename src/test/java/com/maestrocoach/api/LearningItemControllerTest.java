package com.maestrocoach.api;

import com.maestrocoach.domain.LearningItem;
import com.maestrocoach.service.LearningItemService;
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

import static com.maestrocoach.domain.LearningCategory.INSTRUMENT_PRACTICE;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LearningItemController.class)
class LearningItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LearningItemService service;

    @Test
    void createLearningItem_returns201AndBody() throws Exception {
        LearningItem item = new LearningItem("C-Dúr Etűd", INSTRUMENT_PRACTICE, null);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", item.getTitle());
        jsonObject.put("category", item.getCategory().name());
        jsonObject.put("description", item.getDescription());

        Mockito.when(service.createLearningItem(item.getTitle(), item.getCategory(), item.getDescription()))
                .thenReturn(item);

        mockMvc.perform(post("/api/learning-items").contentType(MediaType.APPLICATION_JSON).content(jsonObject.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(item.getId().toString()))
                .andExpect(jsonPath("$.title").value("C-Dúr Etűd"))
                .andExpect(jsonPath("$.category").value("INSTRUMENT_PRACTICE"))
                .andExpect(jsonPath("$.description").value(nullValue()));
    }

    @Test
    void createLearningItem_emptyTitle_returns400() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", "");
        jsonObject.put("category", "INSTRUMENT_PRACTICE");
        jsonObject.put("description", null);

        mockMvc.perform(post("/api/learning-items").contentType(MediaType.APPLICATION_JSON).content(jsonObject.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/learning-items"));
    }

    @Test
    void getAllLearningItems_returns200AndList() throws Exception {
        LearningItem item1 = new LearningItem("C-Dúr Etűd", INSTRUMENT_PRACTICE, null);
        LearningItem item2 = new LearningItem("a-moll Etűd", INSTRUMENT_PRACTICE, "etűd");

        Mockito.when(service.getAllLearningItems())
                .thenReturn(List.of(item1, item2));

        mockMvc.perform(get("/api/learning-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(item1.getId().toString()))
                .andExpect(jsonPath("$[0].title").value("C-Dúr Etűd"))
                .andExpect(jsonPath("$[0].category").value("INSTRUMENT_PRACTICE"))
                .andExpect(jsonPath("$[0].description").value(nullValue()))
                .andExpect(jsonPath("$[1].id").value(item2.getId().toString()))
                .andExpect(jsonPath("$[1].title").value("a-moll Etűd"))
                .andExpect(jsonPath("$[1].category").value("INSTRUMENT_PRACTICE"))
                .andExpect(jsonPath("$[1].description").value("etűd"));
    }

    @Test
    void getLearningItemById_notFound_returns404() throws Exception {
        UUID randomId = UUID.randomUUID();

        Mockito.when(service.getLearningItemById(randomId))
                        .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/learning-items/{learningItemId}", randomId))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteLearningItem_returns204() throws Exception {
        UUID randomId = UUID.randomUUID();

        Mockito.doNothing().when(service).deleteLearningItem(randomId);

        mockMvc.perform(delete("/api/learning-items/{learningItemId}", randomId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteLearningItem_notFound_returns404() throws Exception {
        UUID randomId = UUID.randomUUID();

        Mockito.doThrow(IllegalArgumentException.class).when(service).deleteLearningItem(randomId);

        mockMvc.perform(delete("/api/learning-items/{learningItemId}", randomId))
                .andExpect(status().isNotFound());
    }
}
