package com.maestrocoach.domain;

import java.util.UUID;

public class LearningItem {

    private UUID id;
    private String title;
    private LearningCategory category;
    private String description;

    public LearningItem(String title, LearningCategory category, String description) {
        this.title = title;
        this.category = category;
        this.description = description;
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LearningCategory getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }
}
