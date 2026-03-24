package com.maestrocoach.repository;

import com.maestrocoach.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {
    Page<Student> findByTeacher_Id(UUID teacherId, Pageable pageable);
}
