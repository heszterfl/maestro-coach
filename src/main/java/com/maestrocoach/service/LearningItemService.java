package com.maestrocoach.service;

import com.maestrocoach.domain.LearningCategory;
import com.maestrocoach.domain.LearningItem;
import com.maestrocoach.persistence.InMemoryLearningItemStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LearningItemService {

    private final InMemoryLearningItemStore store;

    public LearningItemService(InMemoryLearningItemStore learningItemStore) {
        this.store = learningItemStore;
    }

    public LearningItem createLearningItem(String title, LearningCategory category, String description) {
        LearningItem item = new LearningItem(title, category, description);
        return store.save(item);
    }

    public Optional<LearningItem> getLearningItemById(UUID id) {
        return store.findById(id);
    }

    public List<LearningItem> getAllLearningItems() {
        return store.findAll();
    }

    public void deleteLearningItem(UUID id) {
        if (store.findById(id).isEmpty()) {
            throw new IllegalArgumentException();
        }

        store.deleteById(id);
    }
}
