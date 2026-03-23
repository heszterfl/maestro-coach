package com.maestrocoach.service;

import com.maestrocoach.api.error.ResourceNotFoundException;
import com.maestrocoach.domain.Assignment;
import com.maestrocoach.domain.AssignmentStatus;
import com.maestrocoach.domain.LearningItem;
import com.maestrocoach.domain.Student;
import com.maestrocoach.repository.AssignmentRepository;
import com.maestrocoach.repository.LearningItemRepository;
import com.maestrocoach.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final StudentRepository studentRepository;
    private final LearningItemRepository learningItemRepository;

    public AssignmentService(AssignmentRepository assignmentRepository, StudentRepository studentRepository, LearningItemRepository learningItemRepository) {
        this.assignmentRepository = assignmentRepository;
        this.studentRepository = studentRepository;
        this.learningItemRepository = learningItemRepository;
    }

    public Assignment createAssignment(UUID studentId, UUID learningItemId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        LearningItem learningItem = learningItemRepository.findById(learningItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Learning item not found with id: " + learningItemId));

        Assignment assignment = new Assignment(student, learningItem);
        return assignmentRepository.save(assignment);
    }

    public List<Assignment> getAssignmentsByStudent(UUID studentId) {
        studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        return assignmentRepository.findByStudent_Id(studentId);
    }

    public List<Assignment> getAssignmentsByStudent(UUID studentId, AssignmentStatus status) {
        studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        return assignmentRepository.findByStudent_IdAndStatus(studentId, status);
    }

    public void completeAssignment(UUID assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + assignmentId));

        assignment.markCompleted();
        assignmentRepository.save(assignment);
    }
}
