package com.maestrocoach.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "learning_items")
public class LearningItem {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LearningCategory category;

    @Column
    private String description;

    public LearningItem() {
    }

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
