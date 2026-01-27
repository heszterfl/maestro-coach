package com.maestrocoach.persistence;

import com.maestrocoach.domain.Assignment;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InMemoryAssignmentStore {

    private final Map<UUID, Assignment> assignments;

    public InMemoryAssignmentStore() {
        assignments = new HashMap<>();
    }

    public Assignment save(Assignment assignment) {
        assignments.put(assignment.getId(), assignment);
        return assignment;
    }

    public Optional<Assignment> findById(UUID id) {
        return Optional.ofNullable(assignments.get(id));
    }

    public List<Assignment> findAll() {
        return new ArrayList<>(assignments.values());
    }

    public List<Assignment> findByStudentId(UUID studentId) {
        List<Assignment> assignmentList = new ArrayList<>();

        for (Assignment assignment : assignments.values()) {
            if (Objects.equals(assignment.getStudentId(), studentId)) {
                assignmentList.add(assignment);
            }
        }
        return assignmentList;
    }

    public void deleteById(UUID id) {
        assignments.remove(id);
    }

    public void clear() {
        assignments.clear();
    }
}
