package com.maestrocoach.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "assignments")
public class Assignment {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "learning_item_id", nullable = false)
    private LearningItem learningItem;

    @Column
    @Enumerated(EnumType.STRING)
    private AssignmentStatus status;

    public Assignment() {
    }

    public Assignment(Student student, LearningItem learningItem) {
        this.id = UUID.randomUUID();
        this.student = student;
        this.learningItem = learningItem;
        this.status = AssignmentStatus.ASSIGNED;
    }

    public void markCompleted() {
        this.status = AssignmentStatus.COMPLETED;
    }

    public UUID getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public LearningItem getLearningItem() {
        return learningItem;
    }

    public AssignmentStatus getStatus() {
        return status;
    }
}
