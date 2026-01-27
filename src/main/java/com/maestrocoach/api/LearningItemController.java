package com.maestrocoach.api;

import com.maestrocoach.api.dto.CreateLearningItemRequest;
import com.maestrocoach.api.dto.LearningItemResponse;
import com.maestrocoach.domain.LearningItem;
import com.maestrocoach.service.LearningItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/learning-items")
public class LearningItemController {

    private final LearningItemService service;

    public LearningItemController(LearningItemService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LearningItemResponse createLearningItem(@RequestBody @Valid CreateLearningItemRequest request) {
        LearningItem item = service.createLearningItem(request.title(), request.category(), request.description());
        return new LearningItemResponse(item.getId(), item.getTitle(), item.getCategory(), item.getDescription());
    }

    @GetMapping
    public List<LearningItemResponse> getAllLearningItems() {
        List<LearningItemResponse> responseList = new ArrayList<>();
        List<LearningItem> itemList = service.getAllLearningItems();
        for (LearningItem item : itemList) {
            LearningItemResponse response = new LearningItemResponse(item.getId(), item.getTitle(), item.getCategory(), item.getDescription());
            responseList.add(response);
        }
        return responseList;
    }

    @GetMapping("/{learningItemId}")
    public LearningItemResponse getLearningItemById(@PathVariable UUID learningItemId) {
        LearningItem item = service.getLearningItemById(learningItemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Learning item not found"));
        return new LearningItemResponse(item.getId(), item.getTitle(), item.getCategory(), item.getDescription());
    }
}
