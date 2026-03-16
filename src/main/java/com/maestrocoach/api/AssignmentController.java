package com.maestrocoach.api;

import com.maestrocoach.api.dto.AssignmentResponse;
import com.maestrocoach.api.dto.CreateAssignmentRequest;
import com.maestrocoach.domain.Assignment;
import com.maestrocoach.service.AssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AssignmentResponse createAssignment(@RequestBody @Valid CreateAssignmentRequest request) {
        Assignment assignment = assignmentService.createAssignment(request.studentId(), request.learningItemId());
        return new AssignmentResponse(assignment.getId(), assignment.getStudent().getId(), assignment.getLearningItem().getId(), assignment.getStatus());
    }

    @PostMapping("/{assignmentId}/complete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void completeAssignment(@PathVariable UUID assignmentId) {
        assignmentService.completeAssignment(assignmentId);
    }
}
