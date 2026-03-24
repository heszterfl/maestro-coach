package com.maestrocoach.service;

import com.maestrocoach.api.error.ResourceNotFoundException;
import com.maestrocoach.domain.LearningCategory;
import com.maestrocoach.domain.LearningItem;
import com.maestrocoach.repository.LearningItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LearningItemService {

    private final LearningItemRepository repository;

    public LearningItemService(LearningItemRepository repository) {
        this.repository = repository;
    }

    public LearningItem createLearningItem(String title, LearningCategory category, String description) {
        LearningItem item = new LearningItem(title, category, description);
        return repository.save(item);
    }

    public LearningItem getLearningItemById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Learning item not found with id: " + id));
    }

    public List<LearningItem> getAllLearningItems() {
        return repository.findAll();
    }

    public void deleteLearningItem(UUID id) {
        LearningItem item = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Learning item not found with id: " + id));

        repository.delete(item);
    }
}
