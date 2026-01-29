package com.maestrocoach.service;

import com.maestrocoach.domain.Assignment;
import com.maestrocoach.domain.AssignmentStatus;
import com.maestrocoach.domain.LearningItem;
import com.maestrocoach.domain.Student;
import com.maestrocoach.persistence.InMemoryAssignmentStore;
import com.maestrocoach.persistence.InMemoryLearningItemStore;
import com.maestrocoach.persistence.InMemoryStudentStore;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static com.maestrocoach.domain.LearningCategory.INSTRUMENT_PRACTICE;
import static org.junit.jupiter.api.Assertions.*;

public class AssignmentServiceTest {

    @Test
    void createAssignment_success() {
        InMemoryAssignmentStore assignmentStore = new InMemoryAssignmentStore();
        InMemoryStudentStore studentStore = new InMemoryStudentStore();
        InMemoryLearningItemStore learningItemStore = new InMemoryLearningItemStore();

        AssignmentService assignmentService = new AssignmentService(assignmentStore, studentStore, learningItemStore);

        Student student = new Student("Anna Bellman", "anna@bellman.com", "piano");
        studentStore.save(student);
        LearningItem learningItem = new LearningItem("C-Dúr Etűd", INSTRUMENT_PRACTICE, null);
        learningItemStore.save(learningItem);

        Assignment assignment = assignmentService.createAssignment(student.getId(), learningItem.getId());

        assertEquals(AssignmentStatus.ASSIGNED, assignment.getStatus());
        assertEquals(student.getId(), assignment.getStudentId());
        assertEquals(learningItem.getId(), assignment.getLearningItemId());
        assertEquals(1, assignmentStore.findAll().size());
    }

    @Test
    void createAssignment_studentNotFound() {
        InMemoryAssignmentStore assignmentStore = new InMemoryAssignmentStore();
        InMemoryStudentStore studentStore = new InMemoryStudentStore();
        InMemoryLearningItemStore learningItemStore = new InMemoryLearningItemStore();

        AssignmentService assignmentService = new AssignmentService(assignmentStore, studentStore, learningItemStore);

        UUID randomStudentId = UUID.randomUUID();
        LearningItem learningItem = new LearningItem("C-Dúr Etűd", INSTRUMENT_PRACTICE, null);
        learningItemStore.save(learningItem);

        assertThrows(IllegalArgumentException.class, () -> assignmentService.createAssignment(randomStudentId, learningItem.getId()));
    }

    @Test
    void createAssignment_learningItemNotFound() {
        InMemoryAssignmentStore assignmentStore = new InMemoryAssignmentStore();
        InMemoryStudentStore studentStore = new InMemoryStudentStore();
        InMemoryLearningItemStore learningItemStore = new InMemoryLearningItemStore();

        AssignmentService assignmentService = new AssignmentService(assignmentStore, studentStore, learningItemStore);

        Student student = new Student("Anna Bellman", "anna@bellman.com", "piano");
        studentStore.save(student);
        UUID randomLearningItemId = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class, () -> assignmentService.createAssignment(student.getId(), randomLearningItemId));
    }

    @Test
    void getAssignmentsByStudent_assigned() {
        InMemoryAssignmentStore assignmentStore = new InMemoryAssignmentStore();
        InMemoryStudentStore studentStore = new InMemoryStudentStore();
        InMemoryLearningItemStore learningItemStore = new InMemoryLearningItemStore();

        AssignmentService assignmentService = new AssignmentService(assignmentStore, studentStore, learningItemStore);

        Student student = new Student("Anna Bellman", "anna@bellman.com", "piano");
        studentStore.save(student);
        LearningItem learningItem1 = new LearningItem("C-Dúr Etűd", INSTRUMENT_PRACTICE, null);
        learningItemStore.save(learningItem1);
        LearningItem learningItem2 = new LearningItem("a-moll Etűd", INSTRUMENT_PRACTICE, "etűd");
        learningItemStore.save(learningItem2);

        assignmentService.createAssignment(student.getId(), learningItem1.getId());
        assignmentService.createAssignment(student.getId(), learningItem2.getId());

        List<Assignment> assignmentList = assignmentService.getAssignmentsByStudent(student.getId(), AssignmentStatus.ASSIGNED);

        assertEquals(2, assignmentList.size());
        assertTrue(assignmentList.stream().allMatch(a -> a.getStudentId().equals(student.getId())));
        assertTrue(assignmentList.stream().allMatch(a -> a.getStatus() == AssignmentStatus.ASSIGNED));
    }

    @Test
    void getAssignmentsByStudent_completed() {
        InMemoryAssignmentStore assignmentStore = new InMemoryAssignmentStore();
        InMemoryStudentStore studentStore = new InMemoryStudentStore();
        InMemoryLearningItemStore learningItemStore = new InMemoryLearningItemStore();

        AssignmentService assignmentService = new AssignmentService(assignmentStore, studentStore, learningItemStore);

        Student student = new Student("Anna Bellman", "anna@bellman.com", "piano");
        studentStore.save(student);
        LearningItem learningItem = new LearningItem("C-Dúr Etűd", INSTRUMENT_PRACTICE, null);
        learningItemStore.save(learningItem);

        Assignment assignment = assignmentService.createAssignment(student.getId(), learningItem.getId());

        assignmentService.completeAssignment(assignment.getId());

        List<Assignment> assignmentList = assignmentService.getAssignmentsByStudent(student.getId(), AssignmentStatus.COMPLETED);

        assertEquals(1, assignmentList.size());
        assertEquals(assignment.getId(), assignmentList.get(0).getId());
        assertTrue(assignmentList.stream().allMatch(a -> a.getStudentId().equals(student.getId())));
        assertTrue(assignmentList.stream().allMatch(a -> a.getStatus() == AssignmentStatus.COMPLETED));
    }

    @Test
    void completeAssignment_marksAssignmentCompleted() {
        InMemoryAssignmentStore assignmentStore = new InMemoryAssignmentStore();
        InMemoryStudentStore studentStore = new InMemoryStudentStore();
        InMemoryLearningItemStore learningItemStore = new InMemoryLearningItemStore();

        AssignmentService assignmentService = new AssignmentService(assignmentStore, studentStore, learningItemStore);

        Assignment assignment = new Assignment(UUID.randomUUID(), UUID.randomUUID());
        assignmentStore.save(assignment);

        assignmentService.completeAssignment(assignment.getId());

        Assignment reloaded = assignmentStore.findById(assignment.getId()).orElseThrow();

        assertEquals(AssignmentStatus.COMPLETED, reloaded.getStatus());
    }

    @Test
    void completeAssignment_notFound_throwsIllegalArgumentException() {
        InMemoryAssignmentStore assignmentStore = new InMemoryAssignmentStore();
        InMemoryStudentStore studentStore = new InMemoryStudentStore();
        InMemoryLearningItemStore learningItemStore = new InMemoryLearningItemStore();

        AssignmentService assignmentService = new AssignmentService(assignmentStore, studentStore, learningItemStore);

        UUID randomId = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class, () -> assignmentService.completeAssignment(randomId));
    }
}
