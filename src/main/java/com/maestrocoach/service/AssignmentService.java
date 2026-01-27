package com.maestrocoach.service;

import com.maestrocoach.domain.Assignment;
import com.maestrocoach.domain.LearningItem;
import com.maestrocoach.domain.Student;
import com.maestrocoach.persistence.InMemoryAssignmentStore;
import com.maestrocoach.persistence.InMemoryLearningItemStore;
import com.maestrocoach.persistence.InMemoryStudentStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AssignmentService {

    private final InMemoryAssignmentStore assignmentStore;
    private final InMemoryStudentStore studentStore;
    private final InMemoryLearningItemStore learningItemStore;

    public AssignmentService(InMemoryAssignmentStore assignmentStore, InMemoryStudentStore studentStore, InMemoryLearningItemStore learningItemStore) {
        this.assignmentStore = assignmentStore;
        this.studentStore = studentStore;
        this.learningItemStore = learningItemStore;
    }

    public Assignment createAssignment(UUID studentId, UUID learningItemId) {
        Optional<Student> studentOptional = studentStore.findById(studentId);
        Optional<LearningItem> learningItemOptional = learningItemStore.findById(learningItemId);

        if (studentOptional.isEmpty() || learningItemOptional.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Assignment assignment = new Assignment(studentId, learningItemId);
        return assignmentStore.save(assignment);
    }

    public List<Assignment> getAssignmentsByStudent(UUID studentId) {
        return assignmentStore.findByStudentId(studentId);
    }
}
