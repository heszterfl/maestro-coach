package com.maestrocoach.repository;

import com.maestrocoach.domain.Assignment;
import com.maestrocoach.domain.AssignmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {
    List<Assignment> findByStudent_Id(UUID studentId);

    List<Assignment> findByStudent_IdAndStatus(UUID studentId, AssignmentStatus status);
}
