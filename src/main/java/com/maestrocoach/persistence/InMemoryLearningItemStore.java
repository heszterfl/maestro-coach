package com.maestrocoach.persistence;

import com.maestrocoach.domain.LearningItem;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InMemoryLearningItemStore {

    private final Map<UUID, LearningItem> items;

    public InMemoryLearningItemStore() {
        this.items = new HashMap<>();
    }

    public LearningItem save(LearningItem item) {
        items.put(item.getId(), item);
        return item;
    }

    public Optional<LearningItem> findById(UUID id) {
        return Optional.ofNullable(items.get(id));
    }

    public List<LearningItem> findAll() {
        return new ArrayList<>(items.values());
    }

    public void deleteById(UUID id) {
        items.remove(id);
    }

    public void clear() {
        items.clear();
    }
}
