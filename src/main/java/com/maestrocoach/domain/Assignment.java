package com.maestrocoach.domain;

import java.util.UUID;

public class Assignment {

    private UUID id;
    private UUID studentId;
    private UUID learningItemId;
    private AssignmentStatus status;

    public Assignment(UUID studentId, UUID learningItemId) {
        this.id = UUID.randomUUID();
        this.studentId = studentId;
        this.learningItemId = learningItemId;
        this.status = AssignmentStatus.ASSIGNED;
    }

    public void markCompleted() {
        this.status = AssignmentStatus.COMPLETED;
    }

    public UUID getId() {
        return id;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public UUID getLearningItemId() {
        return learningItemId;
    }

    public AssignmentStatus getStatus() {
        return status;
    }
}
