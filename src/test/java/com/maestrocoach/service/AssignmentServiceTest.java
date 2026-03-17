package com.maestrocoach.service;

import com.maestrocoach.domain.Assignment;
import com.maestrocoach.domain.AssignmentStatus;
import com.maestrocoach.domain.LearningItem;
import com.maestrocoach.domain.Student;
import com.maestrocoach.repository.AssignmentRepository;
import com.maestrocoach.repository.LearningItemRepository;
import com.maestrocoach.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.maestrocoach.domain.LearningCategory.INSTRUMENT_PRACTICE;
import static org.junit.jupiter.api.Assertions.*;

public class AssignmentServiceTest {

    @Test
    void createAssignment_success() {
        AssignmentRepository assignmentRepository = Mockito.mock(AssignmentRepository.class);
        StudentRepository studentRepository = Mockito.mock(StudentRepository.class);
        LearningItemRepository learningItemRepository = Mockito.mock(LearningItemRepository.class);

        Mockito.when(assignmentRepository.save(Mockito.any(Assignment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AssignmentService assignmentService = new AssignmentService(assignmentRepository, studentRepository, learningItemRepository);

        Student student = new Student("Anna Bellman", "anna@bellman.com", "piano");

        LearningItem learningItem = new LearningItem("C-Dúr Etűd", INSTRUMENT_PRACTICE, null);

        Mockito.when(studentRepository.findById(student.getId()))
                .thenReturn(Optional.of(student));
        Mockito.when(learningItemRepository.findById(learningItem.getId()))
                .thenReturn(Optional.of(learningItem));

        Assignment assignment = assignmentService.createAssignment(student.getId(), learningItem.getId());

        assertEquals(AssignmentStatus.ASSIGNED, assignment.getStatus());
        assertEquals(student.getId(), assignment.getStudent().getId());
        assertEquals(learningItem.getId(), assignment.getLearningItem().getId());

        Mockito.verify(studentRepository).findById(student.getId());
        Mockito.verify(learningItemRepository).findById(learningItem.getId());
        Mockito.verify(assignmentRepository).save(Mockito.any(Assignment.class));
    }

    @Test
    void createAssignment_studentNotFound() {
        AssignmentRepository assignmentRepository = Mockito.mock(AssignmentRepository.class);
        StudentRepository studentRepository = Mockito.mock(StudentRepository.class);
        LearningItemRepository learningItemRepository = Mockito.mock(LearningItemRepository.class);

        AssignmentService assignmentService = new AssignmentService(assignmentRepository, studentRepository, learningItemRepository);

        UUID randomStudentId = UUID.randomUUID();
        LearningItem learningItem = new LearningItem("C-Dúr Etűd", INSTRUMENT_PRACTICE, null);

        Mockito.when(studentRepository.findById(randomStudentId))
                        .thenReturn(Optional.empty());
        Mockito.when(learningItemRepository.findById(learningItem.getId()))
                        .thenReturn(Optional.of(learningItem));

        assertThrows(IllegalArgumentException.class, () -> assignmentService.createAssignment(randomStudentId, learningItem.getId()));

        Mockito.verify(studentRepository).findById(randomStudentId);
    }

    @Test
    void createAssignment_learningItemNotFound() {
        AssignmentRepository assignmentRepository = Mockito.mock(AssignmentRepository.class);
        StudentRepository studentRepository = Mockito.mock(StudentRepository.class);
        LearningItemRepository learningItemRepository = Mockito.mock(LearningItemRepository.class);

        AssignmentService assignmentService = new AssignmentService(assignmentRepository, studentRepository, learningItemRepository);

        Student student = new Student("Anna Bellman", "anna@bellman.com", "piano");
        UUID randomLearningItemId = UUID.randomUUID();

        Mockito.when(studentRepository.findById(student.getId()))
                .thenReturn(Optional.of(student));
        Mockito.when(learningItemRepository.findById(randomLearningItemId))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> assignmentService.createAssignment(student.getId(), randomLearningItemId));

        Mockito.verify(studentRepository).findById(student.getId());
        Mockito.verify(learningItemRepository).findById(randomLearningItemId);
    }

    @Test
    void getAssignmentsByStudent_assigned() {
        AssignmentRepository assignmentRepository = Mockito.mock(AssignmentRepository.class);
        StudentRepository studentRepository = Mockito.mock(StudentRepository.class);
        LearningItemRepository learningItemRepository = Mockito.mock(LearningItemRepository.class);

        Mockito.when(assignmentRepository.save(Mockito.any(Assignment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AssignmentService assignmentService = new AssignmentService(assignmentRepository, studentRepository, learningItemRepository);

        Student student = new Student("Anna Bellman", "anna@bellman.com", "piano");

        LearningItem learningItem1 = new LearningItem("C-Dúr Etűd", INSTRUMENT_PRACTICE, null);
        LearningItem learningItem2 = new LearningItem("a-moll Etűd", INSTRUMENT_PRACTICE, "etűd");

        Mockito.when(studentRepository.findById(student.getId()))
                .thenReturn(Optional.of(student));
        Mockito.when(learningItemRepository.findById(learningItem1.getId()))
                .thenReturn(Optional.of(learningItem1));
        Mockito.when(learningItemRepository.findById(learningItem2.getId()))
                .thenReturn(Optional.of(learningItem2));

        Assignment a1 = assignmentService.createAssignment(student.getId(), learningItem1.getId());
        Assignment a2 = assignmentService.createAssignment(student.getId(), learningItem2.getId());

        Mockito.when(assignmentRepository.findByStudent_IdAndStatus(student.getId(), AssignmentStatus.ASSIGNED))
                .thenReturn(List.of(a1, a2));

        List<Assignment> assignmentList = assignmentService.getAssignmentsByStudent(student.getId(), AssignmentStatus.ASSIGNED);

        assertEquals(2, assignmentList.size());
        assertTrue(assignmentList.stream().allMatch(a -> a.getStudent().getId().equals(student.getId())));
        assertTrue(assignmentList.stream().allMatch(a -> a.getStatus() == AssignmentStatus.ASSIGNED));

        Mockito.verify(assignmentRepository).findByStudent_IdAndStatus(student.getId(), AssignmentStatus.ASSIGNED);
        Mockito.verify(assignmentRepository, Mockito.times(2)).save(Mockito.any(Assignment.class));
    }

    @Test
    void getAssignmentsByStudent_completed() {
        AssignmentRepository assignmentRepository = Mockito.mock(AssignmentRepository.class);
        StudentRepository studentRepository = Mockito.mock(StudentRepository.class);
        LearningItemRepository learningItemRepository = Mockito.mock(LearningItemRepository.class);

        Mockito.when(assignmentRepository.save(Mockito.any(Assignment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AssignmentService assignmentService = new AssignmentService(assignmentRepository, studentRepository, learningItemRepository);

        Student student = new Student("Anna Bellman", "anna@bellman.com", "piano");

        LearningItem learningItem = new LearningItem("C-Dúr Etűd", INSTRUMENT_PRACTICE, null);

        Mockito.when(studentRepository.findById(student.getId()))
                .thenReturn(Optional.of(student));
        Mockito.when(learningItemRepository.findById(learningItem.getId()))
                .thenReturn(Optional.of(learningItem));

        Assignment assignment = assignmentService.createAssignment(student.getId(), learningItem.getId());

        Mockito.when(assignmentRepository.findById(assignment.getId()))
                        .thenReturn(Optional.of(assignment));

        assignmentService.completeAssignment(assignment.getId());

        Mockito.when(assignmentRepository.findByStudent_IdAndStatus(student.getId(), AssignmentStatus.COMPLETED))
                .thenReturn(List.of(assignment));

        List<Assignment> assignmentList = assignmentService.getAssignmentsByStudent(student.getId(), AssignmentStatus.COMPLETED);

        assertEquals(1, assignmentList.size());
        assertEquals(assignment.getId(), assignmentList.get(0).getId());
        assertTrue(assignmentList.stream().allMatch(a -> a.getStudent().getId().equals(student.getId())));
        assertTrue(assignmentList.stream().allMatch(a -> a.getStatus() == AssignmentStatus.COMPLETED));

        Mockito.verify(studentRepository).findById(student.getId());
        Mockito.verify(learningItemRepository).findById(learningItem.getId());
        Mockito.verify(assignmentRepository).findById(assignment.getId());
        Mockito.verify(assignmentRepository).findByStudent_IdAndStatus(student.getId(), AssignmentStatus.COMPLETED);
        Mockito.verify(assignmentRepository, Mockito.times(2)).save(Mockito.any(Assignment.class));
    }

    @Test
    void completeAssignment_marksAssignmentCompleted() {
        AssignmentRepository assignmentRepository = Mockito.mock(AssignmentRepository.class);
        StudentRepository studentRepository = Mockito.mock(StudentRepository.class);
        LearningItemRepository learningItemRepository = Mockito.mock(LearningItemRepository.class);

        Mockito.when(assignmentRepository.save(Mockito.any(Assignment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AssignmentService assignmentService = new AssignmentService(assignmentRepository, studentRepository, learningItemRepository);

        Student student = new Student("Anna Bellman", "anna@bellman.com", "piano");
        LearningItem learningItem = new LearningItem("C-Dúr Etűd", INSTRUMENT_PRACTICE, null);

        Mockito.when(studentRepository.findById(student.getId()))
                .thenReturn(Optional.of(student));
        Mockito.when(learningItemRepository.findById(learningItem.getId()))
                .thenReturn(Optional.of(learningItem));

        Assignment assignment = assignmentService.createAssignment(student.getId(), learningItem.getId());

        Mockito.when(assignmentRepository.findById(assignment.getId()))
                .thenReturn(Optional.of(assignment));

        assignmentService.completeAssignment(assignment.getId());

        Mockito.when(assignmentRepository.findByStudent_IdAndStatus(student.getId(), AssignmentStatus.COMPLETED))
                .thenReturn(List.of(assignment));

        List<Assignment> reloaded = assignmentService.getAssignmentsByStudent(student.getId(), AssignmentStatus.COMPLETED);

        assertEquals(1, reloaded.size());
        assertEquals(AssignmentStatus.COMPLETED, reloaded.get(0).getStatus());

        Mockito.verify(studentRepository).findById(student.getId());
        Mockito.verify(learningItemRepository).findById(learningItem.getId());
        Mockito.verify(assignmentRepository).findById(assignment.getId());
        Mockito.verify(assignmentRepository).findByStudent_IdAndStatus(student.getId(), AssignmentStatus.COMPLETED);
        Mockito.verify(assignmentRepository, Mockito.times(2)).save(Mockito.any(Assignment.class));
    }

    @Test
    void completeAssignment_notFound_throwsIllegalArgumentException() {
        AssignmentRepository assignmentRepository = Mockito.mock(AssignmentRepository.class);
        StudentRepository studentRepository = Mockito.mock(StudentRepository.class);
        LearningItemRepository learningItemRepository = Mockito.mock(LearningItemRepository.class);

        AssignmentService assignmentService = new AssignmentService(assignmentRepository, studentRepository, learningItemRepository);

        UUID randomId = UUID.randomUUID();

        Mockito.when(assignmentRepository.findById(randomId))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> assignmentService.completeAssignment(randomId));

        Mockito.verify(assignmentRepository).findById(randomId);
    }
}
