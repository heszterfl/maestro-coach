package com.maestrocoach.repository;

import com.maestrocoach.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {
    List<Student> findByTeacher_Id(UUID teacherId);
}
